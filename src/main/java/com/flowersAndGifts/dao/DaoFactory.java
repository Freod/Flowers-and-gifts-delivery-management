package com.flowersAndGifts.dao;

import com.flowersAndGifts.dao.impl.OrderDaoImpl;
import com.flowersAndGifts.dao.impl.ProductDaoImpl;
import com.flowersAndGifts.dao.impl.UserDaoImpl;

public class DaoFactory {
    private static final DaoFactory INSTANCE = new DaoFactory();
    private final UserDao userDao;
    private final ProductDao productDao;
    private final OrderDao orderDao;

    private DaoFactory() {
        this.userDao = new UserDaoImpl();
        this.productDao = new ProductDaoImpl();
        this.orderDao = new OrderDaoImpl();
    }

    public static DaoFactory getInstance() {
        return INSTANCE;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }
}
