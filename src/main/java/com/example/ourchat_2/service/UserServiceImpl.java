package com.example.ourchat_2.service;

import com.example.ourchat_2.dao.IUserDao;
import com.example.ourchat_2.dao.UserDaoImpl;
import com.example.ourchat_2.dto.UserDTO;
import com.example.ourchat_2.model.User;
import com.example.ourchat_2.util.PasswordUtil;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserServiceImpl implements IUserService {

    private final IUserDao userDao = new UserDaoImpl();
    private static final Set<Long> onlineUserIds = ConcurrentHashMap.newKeySet();

    @Override
    public boolean register(String nickname, String rawPassword, String email) {
        try {
            User existing = userDao.getUserByEmail(email);
            if (existing != null) return false;

            User user = new User();
            user.setNickname(nickname);
            user.setEmail(email);
            user.setPasswordHash(PasswordUtil.hashPassword(rawPassword));
            return userDao.addUser(user) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public User login(String email, String rawPassword) {
        try {
            List<User> allUsers = userDao.getAllUsers();
            for (User user : allUsers) {
                if (user.getEmail().equalsIgnoreCase(email) &&
                        PasswordUtil.verifyPassword(rawPassword, user.getPasswordHash())) {
                    setUserOnline(user.getUserId());
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserById(Long userId) throws SQLException {
        return userDao.getUserById(userId);
    }

    @Override
    public Map<Long, UserDTO> getAllUsersWithOnlineStatus() throws SQLException {
        List<User> allUsers = userDao.getAllUsers();
        return allUsers.stream()
                .map(u -> new UserDTO(u.getUserId(), u.getNickname(), onlineUserIds.contains(u.getUserId())))
                .collect(Collectors.toMap(UserDTO::getUserId, u -> u));
    }

    @Override
    public Map<Long, UserDTO> getAllUsersWithOfflineStatus() throws SQLException {
        List<User> allUsers = userDao.getAllUsers();
        return allUsers.stream()
                .filter(u -> !onlineUserIds.contains(u.getUserId()))
                .map(u -> new UserDTO(u.getUserId(), u.getNickname(), false))
                .collect(Collectors.toMap(UserDTO::getUserId, u -> u));
    }



    @Override
    public void setUserOnline(Long userId) {
        onlineUserIds.add(userId);
    }

    @Override
    public void setUserOffline(Long userId) {
        onlineUserIds.remove(userId);
    }

    @Override
    public boolean isUserOnline(Long userId) {
        return onlineUserIds.contains(userId);
    }

    @Override
    public int getOnlineUserCount() {
        return onlineUserIds.size();
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        // 调用 DAO 层获取所有用户
        return userDao.getAllUsers();
    }

}
