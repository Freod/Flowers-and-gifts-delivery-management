package com.flowersAndGifts.service;

import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.User;

public interface UserService {
    User login(User user) throws ServiceException;

    User register(User user) throws ServiceException;

    User userAccount(User user) throws ServiceException;

    User changePassword(User user) throws ServiceException;

    User changeActive(User user) throws ServiceException;

    Page<User> allUsersByPage(Page<User> page) throws ServiceException;
}
