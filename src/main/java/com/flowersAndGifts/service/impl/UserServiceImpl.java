package com.flowersAndGifts.service.impl;

import com.flowersAndGifts.dao.UserDao;
import com.flowersAndGifts.dao.impl.UserDaoImpl;
import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.UserService;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    @Override
    public User login(User user) throws ServiceException {
        try {
            return userDao.selectUserByEmailAndPassword(user.getEmail(), user.getPassword());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User register(User user) throws ServiceException {
        try {
            return userDao.insertUser(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User userAccount(User user) throws ServiceException {
        try {
            return userDao.selectUserByEmail(user.getEmail());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User changePassword(User user) throws ServiceException {
        try {
            return userDao.updateUserPassword(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User changeActive(User user) throws ServiceException {
        try {
            return userDao.updateUserActive(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User changeRole(User user) throws ServiceException {
        try {
            return userDao.updateUserRole(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Page<User> allUsersByPage(Page<User> page) throws ServiceException {
        return userDao.selectPageUsers(page);
    }
}