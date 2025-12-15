package com.example.ourchat_2.controller;

import com.example.ourchat_2.model.ChatMessage;
import com.example.ourchat_2.model.User;
import com.example.ourchat_2.service.IChatMessageService;
import com.example.ourchat_2.service.IUserService;
import com.example.ourchat_2.service.ChatMessageServiceImpl;
import com.example.ourchat_2.service.UserServiceImpl;
import com.example.ourchat_2.dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/chatroom")
public class ChatroomServlet extends HttpServlet {

    private final IChatMessageService chatService = new ChatMessageServiceImpl();
    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        System.out.println("userId=" + session.getAttribute("userId"));
        System.out.println("nickname=" + session.getAttribute("nickname"));


        try {
            // 1. 历史消息
            List<ChatMessage> messages = chatService.getAllMessages();
            request.setAttribute("chatMessages", messages);

            // 2. 用户列表
            //在线列表
            Map<Long, UserDTO> usersMap = userService.getAllUsersWithOnlineStatus();
            request.setAttribute("userMap", usersMap);
            // 在线人数
            int onlineCount = userService.getOnlineUserCount();
            request.setAttribute("onlineCount", onlineCount);
            // 离线列表
            Map<Long, UserDTO> offlineMap = userService.getAllUsersWithOfflineStatus();
            request.setAttribute("offlineUserMap", offlineMap);


            // 3. 当前用户信息
            request.setAttribute("userId", session.getAttribute("userId"));
            request.setAttribute("nickname", session.getAttribute("nickname"));

            // 4. 转发到 JSP
            request.getRequestDispatcher("/chatroom.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "服务器错误");
        }
    }
}
