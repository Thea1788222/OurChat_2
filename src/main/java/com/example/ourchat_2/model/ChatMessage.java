package com.example.ourchat_2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Long messageId;
    private Long senderId;
    private String senderNickname;
    private String content;
    private String visibility; // "ALL" 或 "PRIVATE"
    private Long receiverId;   // 私聊接收人，可为 null
    private Timestamp sendTime; // 发送时间
}
