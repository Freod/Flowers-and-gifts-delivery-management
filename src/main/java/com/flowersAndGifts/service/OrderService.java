package com.flowersAndGifts.service;

import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Order;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.User;

public interface OrderService {
    Order showOrder(Order order) throws ServiceException;

    Order makeOrder(Order order) throws ServiceException;

    Order changeOrderStatus(Order order) throws ServiceException;

    Page<Order> allOrdersByPage(Page<Order> page) throws ServiceException;

    Page<Order> allUnsentOrdersByPage(Page<Order> page) throws ServiceException;

    Page<Order> allOrdersByPageAndUserId(Page<Order> page, User user) throws ServiceException;
}