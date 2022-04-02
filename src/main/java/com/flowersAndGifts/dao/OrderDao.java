package com.flowersAndGifts.dao;

import com.flowersAndGifts.model.Order;

import java.util.List;

public interface OrderDao {
    Order selectOrderById(Long id);

    List<Order> selectAllOrders();

    List<Order> selectAllUnsentOrders();

    List<Order> selectAllOrdersByUserId(Long id);

    Order insertOrder(Order order);

    Order updateOrder(Order order);

    void deleteOrder(Order order);
}
