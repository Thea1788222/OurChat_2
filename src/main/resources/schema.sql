-- 使用目标数据库
USE ourchat2;

-- 1. 删除旧表（按依赖顺序删除）
DROP TABLE IF EXISTS system_message;
DROP TABLE IF EXISTS chat_message;
DROP TABLE IF EXISTS user;

-- 2. 创建用户表（单数）
CREATE TABLE IF NOT EXISTS user (
                                    user_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                    nickname VARCHAR(50) NOT NULL,
                                    email VARCHAR(100) NOT NULL UNIQUE,
                                    password_hash VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 创建聊天消息表（单数）
CREATE TABLE IF NOT EXISTS chat_message (
                                            message_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                            sender_id BIGINT UNSIGNED NOT NULL,
                                            sender_nickname VARCHAR(50) NOT NULL,
                                            content TEXT NOT NULL,
                                            visibility ENUM('ALL','PRIVATE') DEFAULT 'ALL',
                                            receiver_id BIGINT UNSIGNED DEFAULT NULL,
                                            send_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            FOREIGN KEY (sender_id) REFERENCES user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 创建系统消息表（单数）
CREATE TABLE IF NOT EXISTS system_message (
                                              system_message_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                              user_id BIGINT UNSIGNED NOT NULL,
                                              nickname VARCHAR(50) NOT NULL,
                                              event_type ENUM('ONLINE','OFFLINE') NOT NULL,
                                              event_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                              FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
