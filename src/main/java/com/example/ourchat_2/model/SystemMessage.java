package com.example.ourchat_2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessage {
    private Long systemMessageId;
    private Long userId;
    private String nickname;
    private String eventType; // "ONLINE" 或 "OFFLINE"
    private Timestamp eventTime; // 事件时间
}
