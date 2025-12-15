package com.example.ourchat_2.service;
import com.example.ourchat_2.dto.UserDTO;
import com.example.ourchat_2.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IUserService {
    boolean register(String nickname,String rawPassword,String email);
    User login(String email,String rawPassword);
    User getUserById(Long userId) throws SQLException;
    Map<Long, UserDTO> getAllUsersWithOnlineStatus() throws SQLException;
    Map<Long, UserDTO> getAllUsersWithOfflineStatus() throws SQLException;
    void setUserOnline(Long userId);
    void setUserOffline(Long userId);
    boolean isUserOnline(Long userId);
    int getOnlineUserCount();
    public List<User> getAllUsers() throws SQLException;
}
