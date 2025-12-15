package com.example.ourchat_2.controller;

import com.example.ourchat_2.model.User;
import com.example.ourchat_2.service.IUserService;
import com.example.ourchat_2.service.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userService.login(email, password);

        if (user != null) {
            HttpSession session = request.getSession();
            // 如果用户已在线，使旧会话失效
            if (userService.isUserOnline(user.getUserId())) {
                userService.setUserOffline(user.getUserId());
            }
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("nickname", user.getNickname());
            userService.setUserOnline(user.getUserId());

            response.sendRedirect(request.getContextPath() + "/chatroom");
        } else {
            request.setAttribute("error", "邮箱或密码错误");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
