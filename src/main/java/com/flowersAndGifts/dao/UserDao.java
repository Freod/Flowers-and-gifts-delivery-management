package com.flowersAndGifts.dao;

import com.flowersAndGifts.model.User;

import java.util.List;

public interface UserDao {
    User selectUserById(Long id);

    User selectUserByLogin(String login);

    List<User> selectAllUsers();

    User insertUser(User user);

    User updateUser(User user);

    void deleteUser();
}
