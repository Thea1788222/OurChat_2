package com.example.ourchat_2.service;

import com.example.ourchat_2.model.SystemMessage;
import java.sql.SQLException;
import java.util.List;

public interface ISystemMessageService {

    int addSystemMessage(SystemMessage message) throws SQLException;

    List<SystemMessage> getAllSystemMessages() throws SQLException;

    SystemMessage getLatestSystemMessage() throws SQLException;
}
