package com.example.ourchat_2.dao;

import com.example.ourchat_2.model.ChatMessage;
import com.example.ourchat_2.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageDaoImpl implements IChatMessageDao {

    @Override
    public int addMessage(ChatMessage message) throws SQLException {
        String sql = "INSERT INTO chat_messages (sender_id, sender_nickname, content, visibility, receiver_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, message.getSenderId());
            ps.setString(2, message.getSenderNickname());
            ps.setString(3, message.getContent());
            ps.setString(4, message.getVisibility());
            if (message.getReceiverId() != null) ps.setLong(5, message.getReceiverId());
            else ps.setNull(5, Types.BIGINT);

            int rows = ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) message.setMessageId(rs.getLong(1));
            }
            return rows;
        }
    }

    @Override
    public List<ChatMessage> getAllMessages() throws SQLException {
        String sql = "SELECT * FROM chat_messages";
        List<ChatMessage> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new ChatMessage(
                        rs.getLong("message_id"),
                        rs.getLong("sender_id"),
                        rs.getString("sender_nickname"),
                        rs.getString("content"),
                        rs.getString("visibility"),
                        rs.getObject("receiver_id") != null ? rs.getLong("receiver_id") : null,
                        rs.getTimestamp("send_time")
                ));
            }
        }
        return list;
    }
}
