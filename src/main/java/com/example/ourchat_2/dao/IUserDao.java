package com.example.ourchat_2.dao;

import com.example.ourchat_2.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDao {

    int addUser(User user) throws SQLException;

    User getUserById(Long userId) throws SQLException;

    List<User> getAllUsers() throws SQLException;

    User getUserByEmail(String email) throws SQLException;

}
