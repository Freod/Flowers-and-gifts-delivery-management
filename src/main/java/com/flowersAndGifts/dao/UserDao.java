package com.flowersAndGifts.dao;

import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.User;

import java.util.List;

public interface UserDao {
    User selectUserById(Long id) throws DaoException;

    User selectUserByEmail(String email) throws DaoException;

    User selectUserByEmailAndPassword(String email, String password) throws DaoException;

    List<User> selectAllUsers() throws DaoException;

    Page<User> selectPageUsers(Page<User> page) throws DaoException;

    User insertUser(User user) throws DaoException;

    User updateUserPassword(User user) throws DaoException;

    User updateUserActive(User user) throws DaoException;

    User updateUserRole(User user) throws DaoException;

    void deleteUserById(Long id) throws DaoException;

    void deleteUserByEmail(String email) throws DaoException;
}
