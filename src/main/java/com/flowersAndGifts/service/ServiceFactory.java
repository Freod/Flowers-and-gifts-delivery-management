package com.flowersAndGifts.service;

import com.flowersAndGifts.service.impl.OrderServiceImpl;
import com.flowersAndGifts.service.impl.ProductServiceImpl;
import com.flowersAndGifts.service.impl.UserServiceImpl;

public class ServiceFactory {
    private static final ServiceFactory INSTANCE = new ServiceFactory();
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;

    private ServiceFactory() {
        this.userService = new UserServiceImpl();
        this.orderService = new OrderServiceImpl();
        this.productService = new ProductServiceImpl();
    }

    public static ServiceFactory getInstance(){
        return INSTANCE;
    }

    public UserService getUserService() {
        return userService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public ProductService getProductService() {
        return productService;
    }
}
