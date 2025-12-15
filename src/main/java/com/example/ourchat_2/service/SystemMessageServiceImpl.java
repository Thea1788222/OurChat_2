package com.example.ourchat_2.service;

import com.example.ourchat_2.dao.ISystemMessageDao;
import com.example.ourchat_2.dao.SystemMessageDaoImpl;
import com.example.ourchat_2.model.SystemMessage;
import com.example.ourchat_2.service.ISystemMessageService;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class SystemMessageServiceImpl implements ISystemMessageService {

    private final ISystemMessageDao systemMessageDao = new SystemMessageDaoImpl();

    @Override
    public int addSystemMessage(SystemMessage message) throws SQLException {
        return systemMessageDao.addSystemMessage(message);
    }

    @Override
    public List<SystemMessage> getAllSystemMessages() throws SQLException {
        return systemMessageDao.getAllSystemMessages();
    }


    @Override
    public SystemMessage getLatestSystemMessage() throws SQLException {
        List<SystemMessage> allMessages = systemMessageDao.getAllSystemMessages();
        return allMessages.stream()
                .max(Comparator.comparing(SystemMessage::getEventTime))
                .orElse(null);
    }
}
