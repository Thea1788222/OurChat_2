package com.example.ourchat_2.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class OnlineUserListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("用户 Session 创建: " + se.getSession().getId());
    }
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("用户 Session 销毁: " + se.getSession().getId());
    }
}
