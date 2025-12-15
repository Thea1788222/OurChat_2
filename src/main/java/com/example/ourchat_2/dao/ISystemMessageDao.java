package com.example.ourchat_2.dao;

import com.example.ourchat_2.model.SystemMessage;

import java.sql.SQLException;
import java.util.List;

public interface ISystemMessageDao {

    int addSystemMessage(SystemMessage message) throws SQLException;

    List<SystemMessage> getAllSystemMessages() throws SQLException;

}
