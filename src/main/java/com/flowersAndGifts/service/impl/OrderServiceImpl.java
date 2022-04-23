package com.flowersAndGifts.service.impl;

import com.flowersAndGifts.dao.OrderDao;
import com.flowersAndGifts.dao.impl.OrderDaoImpl;
import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Order;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.OrderService;

public class OrderServiceImpl implements OrderService {
    OrderDao orderDao = new OrderDaoImpl();

    @Override
    public Order showOrder(Order order) throws ServiceException {
        try {
            return orderDao.selectOrderById(order.getId());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Order makeOrder(Order order) throws ServiceException {
        try {
            return orderDao.insertOrder(order);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Order changeOrderStatus(Order order) throws ServiceException {
        try {
            return orderDao.updateOrder(order);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Page<Order> allOrdersByPage(Page<Order> page) throws ServiceException {
        return orderDao.selectPageOrders(page);
    }

    @Override
    public Page<Order> allUnsentOrdersByPage(Page<Order> page) throws ServiceException {
        return orderDao.selectPageUnsentOrders(page);
    }

    @Override
    public Page<Order> allOrdersByPageAndUserId(Page<Order> page, User user) throws ServiceException {
        return orderDao.selectPageOrdersByUserId(page, user.getId());
    }
}
