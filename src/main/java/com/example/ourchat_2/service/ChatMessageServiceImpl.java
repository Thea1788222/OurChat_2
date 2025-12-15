package com.example.ourchat_2.service;

import com.example.ourchat_2.dao.IChatMessageDao;
import com.example.ourchat_2.dao.ChatMessageDaoImpl;
import com.example.ourchat_2.model.ChatMessage;
import com.example.ourchat_2.service.IChatMessageService;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ChatMessageServiceImpl implements IChatMessageService {

    private final IChatMessageDao messageDao = new ChatMessageDaoImpl();

    @Override
    public int addMessage(ChatMessage message) throws SQLException {
        return messageDao.addMessage(message);
    }

    @Override
    public List<ChatMessage> getAllMessages() throws SQLException {
        return messageDao.getAllMessages();
    }

    @Override
    public List<ChatMessage> getVisibleMessagesForUser(Long userId) throws SQLException {
        List<ChatMessage> allMessages = messageDao.getAllMessages();
        return allMessages.stream()
                .filter(msg -> "ALL".equals(msg.getVisibility()) 
                        || (msg.getReceiverId() != null && msg.getReceiverId().equals(userId))
                        || msg.getSenderId().equals(userId))
                .collect(Collectors.toList());
    }
}
