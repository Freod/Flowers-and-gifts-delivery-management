package com.flowersAndGifts.dao;

import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Order;
import com.flowersAndGifts.model.Page;

import java.util.List;

public interface OrderDao {
    Order selectOrderById(Long id) throws DaoException;

    List<Order> selectAllOrders() throws DaoException;

    List<Order> selectAllUnsentOrders() throws DaoException;

    List<Order> selectAllOrdersByUserId(Long id) throws DaoException;

    Page<Order> selectPageOrders(Page<Order> page) throws DaoException;

    Page<Order> selectPageUnsentOrders(Page<Order> page) throws DaoException;

    Page<Order> selectPageOrdersByUserId(Page<Order> page, Long id) throws DaoException;

    Order insertOrder(Order order) throws DaoException;

    Order updateOrder(Order order) throws DaoException;
}
