package com.flowersAndGifts.dao;

import com.flowersAndGifts.dao.impl.OrderDaoImpl;
import com.flowersAndGifts.dao.impl.ProductDaoImpl;
import com.flowersAndGifts.dao.impl.UserDaoImpl;
import com.flowersAndGifts.model.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderDaoTests {
    private UserDao userDao;
    private ProductDao productDao;
    private OrderDao orderDao;

    private User user = new User("test", "test", "test", "test", Role.CUSTOMER, true);
    private Product product1 = new Product("TestProd1", 20.0, "testProd1.png", true);
    private Product product2 = new Product("TestProd2", 40.0, "testProd2.png", true);
    private Order order = new Order();

    @Before
    public void setUp() {
        //TODO:CLEAR DB
        userDao = new UserDaoImpl();
        productDao = new ProductDaoImpl();
        orderDao = new OrderDaoImpl();
    }

    @Test
    public void OrderDaoTests() throws Exception {
        User userDb = userDao.insertUser(user);

        Product productDb1 = productDao.insertProduct(product1);
        Product productDb2 = productDao.insertProduct(product2);

        order.setAddress(new Address("x", "y", "z", "9"));
        order.setUser_id(userDb.getId());
        order.setSent(false);
        List<ProductOrder> productOrderList = new ArrayList<>();
        productOrderList.add(new ProductOrder(productDb1, 1));
        productOrderList.add(new ProductOrder(productDb2, 2));
        order.setProductOrder(productOrderList);

        Order orderDb1 = orderDao.insertOrder(order);
        Order orderSelect1 = orderDao.selectOrderById(orderDb1.getId());

        assertEquals(orderDb1, orderSelect1);

        List<Order> orders1 = orderDao.selectAllOrders();
        List<Order> orders2 = orderDao.selectAllOrdersByUserId(userDb.getId());
        List<Order> orders3 = orderDao.selectAllUnsentOrders();

        Order orderUpdated = orderDao.updateOrder(orderDb1);

        Order orderSelect2 = orderDao.selectOrderById(orderSelect1.getId());

        System.out.println();
        //TODO:TESTS
    }

    @After
    public void endTests() {
        //TODO:CLEAR DB
    }
}
