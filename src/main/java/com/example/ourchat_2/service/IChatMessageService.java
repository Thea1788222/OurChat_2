package com.example.ourchat_2.service;

import com.example.ourchat_2.model.ChatMessage;
import java.sql.SQLException;
import java.util.List;

public interface IChatMessageService {

    int addMessage(ChatMessage message) throws SQLException;
    List<ChatMessage> getAllMessages() throws SQLException;
    List<ChatMessage> getVisibleMessagesForUser(Long userId) throws SQLException;
}
