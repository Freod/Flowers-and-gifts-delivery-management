package com.flowersAndGifts.dao.impl;

import com.flowersAndGifts.dao.OrderDao;
import com.flowersAndGifts.dao.ProductDao;
import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Address;
import com.flowersAndGifts.model.Order;
import com.flowersAndGifts.model.Product;
import com.flowersAndGifts.model.ProductOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderDaoImpl extends AbstractDao implements OrderDao {
    private static final String SELECT_ORDER_BY_ID_QUERY = "SELECT o.id, o.user_id, o.country, o.address, o.city, o.postcode, o.send, po.product_id, p.name, p.price, p.image, p.active, po.amount FROM ORDERS o LEFT JOIN PRODUCTS_ORDERS po ON o.ID=po.ORDER_ID LEFT JOIN PRODUCTS p ON p.ID=po.PRODUCT_ID WHERE o.id = ?;";
    private static final String SELECT_ALL_ORDERS_QUERY = "SELECT o.id, o.user_id, o.country, o.address, o.city, o.postcode, o.send FROM ORDERS o";
    private static final String SELECT_ALL_ORDERS_UNSENT_QUERY = "SELECT o.id, o.user_id, o.country, o.address, o.city, o.postcode, o.send FROM ORDERS o WHERE o.send = ?";
    private static final String SELECT_ALL_ORDERS_BY_USER_ID_QUERY = "SELECT o.id, o.user_id, o.country, o.address, o.city, o.postcode, o.send FROM ORDERS o WHERE o.user_id = ?";
    private static final String SELECT_ALL_PRODUCTS_ORDER_BY_ORDER_ID_QUERY = "SELECT po.product_id, p.name, p.price, p.image, p.active, po.amount FROM PRODUCTS_ORDERS po LEFT JOIN PRODUCTS p ON p.ID=po.PRODUCT_ID WHERE po.order_id = ?;";
    private static final String INSERT_ORDER_QUERY = "INSERT INTO ORDERS (user_id, country, address, city, postcode, send) VALUES (?, ?, ?, ?, ?, ?) RETURNING id;";
    private static final String INSERT_PRODUCTS_ORDER_QUERY = "INSERT INTO PRODUCTS_ORDERS (order_id, product_id, amount) VALUES (?, ?, ?);";
    private static final String UPDATE_ORDER_QUERY = "UPDATE ORDERS SET user_id=?, country=?, address=?, city=?, postcode=?, send=? WHERE id = ?;";

    @Override
    public Order selectOrderById(Long id) throws DaoException {
        Order order;
        try (Connection connection = getConnection()) {
            try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_ORDER_BY_ID_QUERY, Collections.singletonList(id))) {
                try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
                    List<ProductOrder> productOrderList = new ArrayList<>();
                    if (selectResultSet.next()) {
                        order = new Order(
                                selectResultSet.getLong("ID"),
                                selectResultSet.getLong("USER_ID"),
                                null,
                                new Address(
                                        selectResultSet.getString("COUNTRY"),
                                        selectResultSet.getString("ADDRESS"),
                                        selectResultSet.getString("CITY"),
                                        selectResultSet.getString("POSTCODE")
                                ),
                                selectResultSet.getBoolean("SEND")
                        );
                        do {
                            ProductOrder productOrder = new ProductOrder(
                                    new Product(
                                            selectResultSet.getLong("PRODUCT_ID"),
                                            selectResultSet.getString("NAME"),
                                            selectResultSet.getDouble("PRICE"),
                                            selectResultSet.getString("IMAGE"),
                                            selectResultSet.getBoolean("ACTIVE")
                                    ),
                                    selectResultSet.getInt("AMOUNT")
                            );
                            productOrderList.add(productOrder);
                        } while (selectResultSet.next());
                    } else {
                        throw new DaoException("Order with this id does not exist.");
                    }
                    order.setProductOrder(productOrderList);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return order;
    }

    @Override
    public List<Order> selectAllOrders() {
        return getAllOrders(SELECT_ALL_ORDERS_QUERY, new ArrayList<>());
    }

    @Override
    public List<Order> selectAllUnsentOrders() {
        return getAllOrders(SELECT_ALL_ORDERS_UNSENT_QUERY, Collections.singletonList(false));
    }

    @Override
    public List<Order> selectAllOrdersByUserId(Long id) {
        return getAllOrders(SELECT_ALL_ORDERS_BY_USER_ID_QUERY, Collections.singletonList(id));
    }

    private List<Order> getAllOrders(String selectAllOrdersQuery, List<Object> parameters) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = getConnection()) {
            try (PreparedStatement selectAllOrdersPreparedStatement = getPreparedStatement(connection, selectAllOrdersQuery, parameters)) {
                try (ResultSet selectAllOrdersResultSet = selectAllOrdersPreparedStatement.executeQuery()) {

                    while (selectAllOrdersResultSet.next()) {
                        Order order = new Order(
                                selectAllOrdersResultSet.getLong("ID"),
                                selectAllOrdersResultSet.getLong("USER_ID"),
                                null,
                                new Address(
                                        selectAllOrdersResultSet.getString("COUNTRY"),
                                        selectAllOrdersResultSet.getString("ADDRESS"),
                                        selectAllOrdersResultSet.getString("CITY"),
                                        selectAllOrdersResultSet.getString("POSTCODE")
                                ),
                                selectAllOrdersResultSet.getBoolean("send")
                        );
                        orders.add(order);
                    }
                    for (Order order : orders) {
                        List<ProductOrder> productOrderList = new ArrayList<>();
                        try (PreparedStatement selectProductsOrderPreparedStatement = getPreparedStatement(connection, SELECT_ALL_PRODUCTS_ORDER_BY_ORDER_ID_QUERY, Collections.singletonList(order.getId()))) {
                            try (ResultSet selectAllProductsOrderResultSet = selectProductsOrderPreparedStatement.executeQuery()) {
                                while (selectAllProductsOrderResultSet.next()) {
                                    ProductOrder productOrder = new ProductOrder(
                                            new Product(
                                                    selectAllProductsOrderResultSet.getLong("PRODUCT_ID"),
                                                    selectAllProductsOrderResultSet.getString("NAME"),
                                                    selectAllProductsOrderResultSet.getDouble("PRICE"),
                                                    selectAllProductsOrderResultSet.getString("IMAGE"),
                                                    selectAllProductsOrderResultSet.getBoolean("ACTIVE")
                                            ),
                                            selectAllProductsOrderResultSet.getInt("AMOUNT")
                                    );
                                    productOrderList.add(productOrder);
                                }
                            }
                        }
                        order.setProductOrder(productOrderList);
                    }
                }
            }
        } catch (
                SQLException exception) {
            throw new RuntimeException(exception);
        }

        return orders;
    }

    @Override
    public Order insertOrder(Order order) throws DaoException {
        List<Object> insertOrderParameters = Arrays.asList(
                order.getUser_id(),
                order.getAddress().getCountry(),
                order.getAddress().getAddress(),
                order.getAddress().getCity(),
                order.getAddress().getPostcode(),
                order.isSent()
        );

        try (Connection connection = getConnection()) {
            ProductDao productDao = new ProductDaoImpl();
            for (ProductOrder productOrder : order.getProductOrder()) {
                productDao.selectProductById(productOrder.getProduct().getId());
            }
            try (PreparedStatement insertOrderPreparedStatement = getPreparedStatement(connection, INSERT_ORDER_QUERY, insertOrderParameters)) {
                try (ResultSet resultSetInsertOrder = insertOrderPreparedStatement.executeQuery()) {
                    if (resultSetInsertOrder.next()) {
                        order.setId(resultSetInsertOrder.getLong("ID"));
                    } else {
                        throw new DaoException("Order does not inserted");
                    }
                }
            }
            for (ProductOrder productOrder : order.getProductOrder()) {
                List<Object> insertProductsOrderParameters = Arrays.asList(
                        order.getId(),
                        productOrder.getProduct().getId(),
                        productOrder.getAmount()
                );
                try (PreparedStatement insertProductsOrderPreparedStatement = getPreparedStatement(connection, INSERT_PRODUCTS_ORDER_QUERY, insertProductsOrderParameters)) {
                    insertProductsOrderPreparedStatement.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    @Override
    public Order updateOrder(Order order) throws DaoException {
        List<Object> parameters = Arrays.asList(
                order.getUser_id(),
                order.getAddress().getCountry(),
                order.getAddress().getAddress(),
                order.getAddress().getCity(),
                order.getAddress().getPostcode(),
                order.isSent(),
                order.getId()
        );

        try (Connection connection = getConnection()) {
            try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_ORDER_BY_ID_QUERY, Collections.singletonList(order.getId()))) {
                try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
                    if (selectResultSet.next()) {
                        try (PreparedStatement updatePreparedStatement = getPreparedStatement(connection, UPDATE_ORDER_QUERY, parameters)) {
                            updatePreparedStatement.execute();
                        }
                    } else {
                        throw new DaoException("Order with this id does not exits.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }
}
