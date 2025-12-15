package com.example.ourchat_2.dao;

import com.example.ourchat_2.model.ChatMessage;

import java.sql.SQLException;
import java.util.List;

public interface IChatMessageDao {

    int addMessage(ChatMessage message) throws SQLException;
    List<ChatMessage> getAllMessages() throws SQLException;

}
