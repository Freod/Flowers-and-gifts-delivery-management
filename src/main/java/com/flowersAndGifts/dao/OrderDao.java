package com.flowersAndGifts.dao;

import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Order;

import java.util.List;

public interface OrderDao {
    Order selectOrderById(Long id) throws DaoException;

    List<Order> selectAllOrders();

    List<Order> selectAllUnsentOrders();

    List<Order> selectAllOrdersByUserId(Long id);

    Order insertOrder(Order order) throws DaoException;

    Order updateOrder(Order order) throws DaoException;
}
