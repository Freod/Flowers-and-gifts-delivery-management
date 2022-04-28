package com.flowersAndGifts.dao.impl;

import com.flowersAndGifts.dao.OrderDao;
import com.flowersAndGifts.dao.ProductDao;
import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.*;

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
    private static final String SELECT_PAGE_ORDERS_QUERY = "SELECT o.id, o.user_id, o.country, o.address, o.city, o.postcode, o.send FROM ORDERS o " +
            " WHERE UPPER(o.country) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.address) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.city) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.postcode) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " ORDER BY o.%s %s LIMIT ? OFFSET ?";
    private static final String SELECT_PAGE_ORDERS_UNSENT_QUERY = "SELECT o.id, o.user_id, o.country, o.address, o.city, o.postcode, o.send FROM ORDERS o " +
            " WHERE UPPER(o.country) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.address) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.city) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.postcode) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND o.send = false" +
            " ORDER BY o.%s %s LIMIT ? OFFSET ?";
    private static final String SELECT_PAGE_ORDERS_BY_USER_ID_QUERY = "SELECT o.id, o.user_id, o.country, o.address, o.city, o.postcode, o.send FROM ORDERS o " +
            " WHERE UPPER(o.country) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.address) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.city) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.postcode) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND o.user_id = ?" +
            " ORDER BY o.%s %s LIMIT ? OFFSET ?";
    private static final String COUNT_PAGE_ORDERS_QUERY = "SELECT count(o.id) FROM ORDERS o " +
            " WHERE UPPER(o.country) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.address) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.city) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.postcode) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND o.send = false";
    private static final String COUNT_PAGE_ORDERS_UNSENT_QUERY = "SELECT count(o.id) FROM ORDERS o " +
            " WHERE UPPER(o.country) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.address) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.city) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.postcode) LIKE CONCAT('%%', UPPER(?), '%%')";
    private static final String COUNT_PAGE_ORDERS_BY_USER_ID_QUERY = "SELECT count(o.id) FROM ORDERS o " +
            " WHERE UPPER(o.country) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.address) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.city) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(o.postcode) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND o.user_id = ?";
    private static final String INSERT_ORDER_QUERY = "INSERT INTO ORDERS (user_id, country, address, city, postcode, send) VALUES (?, ?, ?, ?, ?, ?) RETURNING id;";
    private static final String INSERT_PRODUCTS_ORDER_QUERY = "INSERT INTO PRODUCTS_ORDERS (order_id, product_id, amount) VALUES (?, ?, ?);";
    private static final String UPDATE_ORDER_QUERY = "UPDATE ORDERS SET user_id=?, country=?, address=?, city=?, postcode=?, send=? WHERE id = ?;";

    @Override
    public Order selectOrderById(Long id) throws DaoException {
        Order order;
        try (Connection connection = getConnection();
             PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_ORDER_BY_ID_QUERY, Collections.singletonList(id));
             ResultSet selectResultSet = selectPreparedStatement.executeQuery()
        ) {
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
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return order;
    }

    @Override
    public List<Order> selectAllOrders() throws DaoException {
        return getAllOrders(SELECT_ALL_ORDERS_QUERY, new ArrayList<>());
    }

    @Override
    public List<Order> selectAllUnsentOrders() throws DaoException {
        return getAllOrders(SELECT_ALL_ORDERS_UNSENT_QUERY, Collections.singletonList(false));
    }

    @Override
    public List<Order> selectAllOrdersByUserId(Long id) throws DaoException {
        return getAllOrders(SELECT_ALL_ORDERS_BY_USER_ID_QUERY, Collections.singletonList(id));
    }

    private List<Order> getAllOrders(String selectAllOrdersQuery, List<Object> parameters) throws DaoException {
        List<Order> orders = new ArrayList<>();

        Connection connection = getConnection();
        try (PreparedStatement selectAllOrdersPreparedStatement = getPreparedStatement(connection, selectAllOrdersQuery, parameters);
             ResultSet selectAllOrdersResultSet = selectAllOrdersPreparedStatement.executeQuery()
        ) {
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
            getOrderWithProducts(connection, orders);
        } catch (SQLException exception) {
            throw new DaoException(exception);
        } finally {
            closeConnection(connection);
        }

        return orders;
    }

    @Override
    public Page<Order> selectPageOrders(Page<Order> page) throws DaoException {
        return getPageOrders(SELECT_PAGE_ORDERS_QUERY, COUNT_PAGE_ORDERS_QUERY, page, null);
    }

    @Override
    public Page<Order> selectPageUnsentOrders(Page<Order> page) throws DaoException {
        return getPageOrders(SELECT_PAGE_ORDERS_UNSENT_QUERY, COUNT_PAGE_ORDERS_UNSENT_QUERY, page, null);
    }

    @Override
    public Page<Order> selectPageOrdersByUserId(Page<Order> page, Long id) throws DaoException {
        return getPageOrders(SELECT_PAGE_ORDERS_BY_USER_ID_QUERY, COUNT_PAGE_ORDERS_BY_USER_ID_QUERY, page, id);
    }

    private Page<Order> getPageOrders(String selectPageOrdersQuery, String countPageOrdersQuery, Page<Order> page, Long id) throws DaoException {
        List<Object> parameters;
        List<Object> parameters2;

        if (id == null) {
            parameters = Arrays.asList(
                    page.getFilter().getAddress().getCountry(),
                    page.getFilter().getAddress().getAddress(),
                    page.getFilter().getAddress().getCity(),
                    page.getFilter().getAddress().getPostcode(),
                    page.getPageSize(),
                    page.getOffset()
            );
            parameters2 = Arrays.asList(
                    page.getFilter().getAddress().getCountry(),
                    page.getFilter().getAddress().getAddress(),
                    page.getFilter().getAddress().getCity(),
                    page.getFilter().getAddress().getPostcode()
            );
        } else {
            parameters = Arrays.asList(
                    page.getFilter().getAddress().getCountry(),
                    page.getFilter().getAddress().getAddress(),
                    page.getFilter().getAddress().getCity(),
                    page.getFilter().getAddress().getPostcode(),
                    id,
                    page.getPageSize(),
                    page.getOffset()
            );
            parameters2 = Arrays.asList(
                    page.getFilter().getAddress().getCountry(),
                    page.getFilter().getAddress().getAddress(),
                    page.getFilter().getAddress().getCity(),
                    page.getFilter().getAddress().getPostcode(),
                    id
            );
        }

        Connection connection = getConnection();
        try (PreparedStatement preparedStatement = getPreparedStatement(connection, setSortAndDirection(selectPageOrdersQuery, page.getSortBy(), page.getDirection()), parameters);
             ResultSet selectResultSet = preparedStatement.executeQuery();
             PreparedStatement countPreparedStatement = getPreparedStatement(connection, setSortAndDirection(countPageOrdersQuery, page.getSortBy(), page.getDirection()), parameters2);
             ResultSet countResultSet = countPreparedStatement.executeQuery()
        ) {
            List<Order> orders = new ArrayList<>();
            while (selectResultSet.next()) {
                Order order = new Order(
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
                orders.add(order);
            }
            getOrderWithProducts(connection, orders);
            if (countResultSet.next()) {
                page.setTotalElements(countResultSet.getLong(1));
            }
            page.setElements(orders);
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeConnection(connection);
        }
        return page;
    }

    private List<Order> getOrderWithProducts(Connection connection, List<Order> orders) throws SQLException {
        for (Order order : orders) {
            List<ProductOrder> productOrderList = new ArrayList<>();
            try (PreparedStatement selectProductsOrderPreparedStatement = getPreparedStatement(connection, SELECT_ALL_PRODUCTS_ORDER_BY_ORDER_ID_QUERY, Collections.singletonList(order.getId()));
                 ResultSet selectAllProductsOrderResultSet = selectProductsOrderPreparedStatement.executeQuery()
            ) {
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
            order.setProductOrder(productOrderList);
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
                order.getStatus()
        );

        Connection connection = getConnection();
        try {
            ProductDao productDao = new ProductDaoImpl();
            for (ProductOrder productOrder : order.getProductOrder()) {
                productDao.selectProductById(productOrder.getProduct().getId());
            }
            try (PreparedStatement insertOrderPreparedStatement = getPreparedStatement(connection, INSERT_ORDER_QUERY, insertOrderParameters);
                 ResultSet resultSetInsertOrder = insertOrderPreparedStatement.executeQuery()
            ) {
                if (resultSetInsertOrder.next()) {
                    order.setId(resultSetInsertOrder.getLong("ID"));
                } else {
                    throw new DaoException("Order does not inserted");
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
            throw new DaoException(e);
        } finally {
            closeConnection(connection);
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
                order.getStatus(),
                order.getId()
        );

        Connection connection = getConnection();
        try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_ORDER_BY_ID_QUERY, Collections.singletonList(order.getId()));
             ResultSet selectResultSet = selectPreparedStatement.executeQuery()
        ) {
            if (selectResultSet.next()) {
                try (PreparedStatement updatePreparedStatement = getPreparedStatement(connection, UPDATE_ORDER_QUERY, parameters)) {
                    updatePreparedStatement.execute();
                }
            } else {
                throw new DaoException("Order with this id does not exits.");
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeConnection(connection);
        }
        return order;
    }
}
