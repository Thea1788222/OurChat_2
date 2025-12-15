package com.example.ourchat_2.listener;

import com.example.ourchat_2.model.SystemMessage;
import com.example.ourchat_2.service.ISystemMessageService;
import com.example.ourchat_2.service.IUserService;
import com.example.ourchat_2.service.UserServiceImpl;
import com.example.ourchat_2.service.SystemMessageServiceImpl;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.sql.Timestamp;

@WebListener
public class OnlineUserListener implements HttpSessionListener {

    private final IUserService userService = new UserServiceImpl();
    private final ISystemMessageService systemMessageService = new SystemMessageServiceImpl();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Long userId = (Long) se.getSession().getAttribute("userId");
        String nickname = (String) se.getSession().getAttribute("nickname");

        if (userId != null) {
            // 设置用户离线
            userService.setUserOffline(userId);

            // 添加系统消息
            SystemMessage message = new SystemMessage();
            message.setUserId(userId);
            message.setNickname(nickname);
            message.setEventType("OFFLINE");
            message.setEventTime(new Timestamp(System.currentTimeMillis()));

            try {
                systemMessageService.addSystemMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
