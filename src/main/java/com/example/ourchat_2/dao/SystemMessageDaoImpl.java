package com.example.ourchat_2.dao;

import com.example.ourchat_2.model.SystemMessage;
import com.example.ourchat_2.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemMessageDaoImpl implements ISystemMessageDao {

    @Override
    public int addSystemMessage(SystemMessage message) throws SQLException {
        String sql = "INSERT INTO system_messages (user_id, nickname, event_type) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, message.getUserId());
            ps.setString(2, message.getNickname());
            ps.setString(3, message.getEventType());

            int rows = ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) message.setSystemMessageId(rs.getLong(1));
            }
            return rows;
        }
    }

    @Override
    public List<SystemMessage> getAllSystemMessages() throws SQLException {
        String sql = "SELECT * FROM system_messages";
        List<SystemMessage> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new SystemMessage(
                        rs.getLong("system_message_id"),
                        rs.getLong("user_id"),
                        rs.getString("nickname"),
                        rs.getString("event_type"),
                        rs.getTimestamp("event_time")
                ));
            }
        }
        return list;
    }
}
