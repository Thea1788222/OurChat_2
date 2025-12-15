package com.example.ourchat_2.controller;

import com.example.ourchat_2.model.User;
import com.example.ourchat_2.service.IUserService;
import com.example.ourchat_2.service.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求编码
        req.setCharacterEncoding("UTF-8");

        // 获取表单参数
        String nickname = req.getParameter("nickname").trim();
        String email = req.getParameter("email").trim();
        String password = req.getParameter("password").trim();

        // 调用 Service 注册
        boolean success = userService.register(nickname, password, email);

        if (success) {
            // 注册成功，跳转登录页
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            // 注册失败（邮箱已存在）
            req.setAttribute("error", "注册失败，邮箱可能已存在");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }

    // 可选：支持 GET 请求直接访问注册页
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }
}
