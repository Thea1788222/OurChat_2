package com.example.ourchat_2.websocket;

import com.example.ourchat_2.dto.UserDTO;
import com.example.ourchat_2.model.ChatMessage;
import com.example.ourchat_2.model.SystemMessage;
import com.example.ourchat_2.model.User;
import com.example.ourchat_2.service.*;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.*;

@ServerEndpoint(value = "/chatSocket", configurator = GetHttpSessionConfigurator.class)
public class ChatWebSocket {

    private static final Map<Long, Session> onlineSessions = Collections.synchronizedMap(new HashMap<>());

    private Session session;
    private Long userId;
    private String nickname;

    private final IUserService userService = new UserServiceImpl();
    private final ISystemMessageService systemMessageService = new SystemMessageServiceImpl();
    private final IChatMessageService chatMessageService = new ChatMessageServiceImpl();
    private final Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.userId = (Long) httpSession.getAttribute("userId");
        this.nickname = (String) httpSession.getAttribute("nickname");

        // 设置在线
        userService.setUserOnline(userId);
        onlineSessions.put(userId, session);

        // 广播上线系统消息和更新列表
        broadcastSystemEvent("ONLINE");
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            Gson gson = new Gson();
            Map<String, Object> map = gson.fromJson(message, Map.class);

            ChatMessage chatMessage = new ChatMessage();

            // 处理 senderId
            Object senderIdObj = map.get("senderId");
            long senderId = 0;
            if (senderIdObj instanceof Number) {
                senderId = ((Number) senderIdObj).longValue();
            } else if (senderIdObj instanceof String) {
                senderId = Long.parseLong((String) senderIdObj);
            }
            chatMessage.setSenderId(senderId);

            // 昵称
            chatMessage.setSenderNickname((String) map.get("senderNickname"));

            // 消息内容
            chatMessage.setContent((String) map.get("content"));

            // 发送时间
            chatMessage.setSendTime(new Timestamp(System.currentTimeMillis()));
            chatMessage.setVisibility("ALL");

            // 保存并广播
            chatMessageService.addMessage(chatMessage);
            broadcastChatMessage(chatMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @OnClose
    public void onClose() {
        onlineSessions.remove(userId);
        userService.setUserOffline(userId);

        // 广播下线系统消息和更新列表
        broadcastSystemEvent("OFFLINE");
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    /** 广播系统事件（上线/下线）并发送最新在线/离线用户列表 */
    private void broadcastSystemEvent(String eventType) {
        SystemMessage msg = new SystemMessage();
        msg.setUserId(userId);
        msg.setNickname(nickname);
        msg.setEventType(eventType);
        msg.setEventTime(new Timestamp(System.currentTimeMillis()));

        try {
            systemMessageService.addSystemMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 构建系统消息 JSON
        Map<String, Object> jsonMsg = new HashMap<>();
        jsonMsg.put("type", "system");
        jsonMsg.put("userId", userId);
        jsonMsg.put("nickname", nickname);
        jsonMsg.put("status", eventType);
        jsonMsg.put("time", msg.getEventTime().getTime());

        // 使用 onlineSessions 构建在线用户列表
        List<Map<String, Object>> onlineUsers = new ArrayList<>();
        List<Map<String, Object>> offlineUsers = new ArrayList<>();

        try {
            List<User> allUsers = userService.getAllUsers(); // 从数据库获取所有用户

            for (User user : allUsers) {
                Map<String, Object> u = new HashMap<>();
                u.put("userId", user.getUserId());
                u.put("nickname", user.getNickname());
                boolean isOnline = onlineSessions.containsKey(user.getUserId());
                u.put("online", isOnline);

                if (isOnline) onlineUsers.add(u);
                else offlineUsers.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        jsonMsg.put("onlineUsers", onlineUsers);
        jsonMsg.put("offlineUsers", offlineUsers);

        sendToAllSessions(gson.toJson(jsonMsg));
    }


    /** 广播聊天消息 */
    private void broadcastChatMessage(ChatMessage msg) {
        Map<String, Object> jsonMsg = new HashMap<>();
        jsonMsg.put("type", "chat");
        jsonMsg.put("senderId", msg.getSenderId());
        jsonMsg.put("senderNickname", msg.getSenderNickname());
        jsonMsg.put("content", msg.getContent());
        jsonMsg.put("time", msg.getSendTime().getTime());

        sendToAllSessions(gson.toJson(jsonMsg));
    }

    /** 工具方法：发送 JSON 给所有在线用户 */
    private void sendToAllSessions(String json) {
        synchronized (onlineSessions) {
            onlineSessions.values().forEach(s -> {
                try {
                    s.getBasicRemote().sendText(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

