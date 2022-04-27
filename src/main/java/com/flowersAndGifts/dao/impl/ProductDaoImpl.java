package com.flowersAndGifts.dao.impl;

import com.flowersAndGifts.dao.ProductDao;
import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductDaoImpl extends AbstractDao implements ProductDao {

    private static final String SELECT_PRODUCT_BY_ID_QUERY = "SELECT p.id, p.name, p.price, p.image, p.active FROM PRODUCTS p WHERE id = ?";
    private static final String SELECT_PRODUCT_BY_NAME_QUERY = "SELECT p.id, p.name, p.price, p.image, p.active FROM PRODUCTS p WHERE name = ?";
    private static final String SELECT_ALL_PRODUCTS_QUERY = "SELECT p.id, p.name, p.price, p.image, p.active FROM PRODUCTS p";
    private static final String SELECT_ALL_ACTIVE_PRODUCTS_QUERY = "SELECT p.id, p.name, p.price, p.image, p.active FROM PRODUCTS p WHERE p.active = true";
    private static final String SELECT_PAGE_PRODUCTS_QUERY = "SELECT p.id, p.name, p.price, p.image, p.active FROM PRODUCTS p" +
            " WHERE UPPER(p.name) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " ORDER BY p.%s %s LIMIT ? OFFSET ?";
    private static final String SELECT_PAGE_ACTIVE_PRODUCTS_QUERY = "SELECT p.id, p.name, p.price, p.image, p.active FROM PRODUCTS p" +
            " WHERE UPPER(p.name) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND p.active = true" +
            " ORDER BY p.%s %s LIMIT ? OFFSET ?";
    private static final String COUNT_PAGE_PRODUCTS_QUERY = "SELECT COUNT(p.id) FROM PRODUCTS p" +
            " WHERE UPPER(p.name) LIKE CONCAT('%%', UPPER(?), '%%')";
    private static final String COUNT_PAGE_ACTIVE_PRODUCTS_QUERY = "SELECT COUNT(p.id) FROM PRODUCTS p" +
            " WHERE UPPER(p.name) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND p.active = true";
    private static final String INSERT_PRODUCT_QUERY = "INSERT INTO PRODUCTS (name, price, image, active) VALUES(?, ?, ?, ?) RETURNING id";
    private static final String UPDATE_PRODUCT_QUERY = "UPDATE PRODUCTS SET price = ?, image = ?, active = ? WHERE id = ?";

    @Override
    public Product selectProductById(Long id) throws DaoException {
        return getProduct(SELECT_PRODUCT_BY_ID_QUERY, Collections.singletonList(id));
    }

    @Override
    public Product selectProductByName(String name) throws DaoException {
        return getProduct(SELECT_PRODUCT_BY_NAME_QUERY, Collections.singletonList(name));
    }

    private Product getProduct(String selectProductQuery, List<Object> parameter) throws DaoException {
        Product product;

        try (Connection connection = getConnection();
             PreparedStatement selectPreparedStatement = getPreparedStatement(connection, selectProductQuery, parameter);
             ResultSet selectResultSet = selectPreparedStatement.executeQuery()
        ) {
            if (selectResultSet.next()) {
                product = new Product(
                        selectResultSet.getLong("ID"),
                        selectResultSet.getString("NAME"),
                        selectResultSet.getDouble("PRICE"),
                        selectResultSet.getString("IMAGE"),
                        selectResultSet.getBoolean("ACTIVE")
                );
            } else {
                if (parameter.get(0).getClass() == Long.class) {
                    throw new DaoException("Product with this id does not exist.");
                } else {
                    throw new DaoException("Product with this name does not exist.");
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return product;
    }

    @Override
    public List<Product> selectAllProducts() {
        return getAllProducts(SELECT_ALL_PRODUCTS_QUERY);
    }

    @Override
    public List<Product> selectAllActiveProducts() {
        return getAllProducts(SELECT_ALL_ACTIVE_PRODUCTS_QUERY);
    }

    private List<Product> getAllProducts(String selectAllProductsQuery) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet selectResultSet = statement.executeQuery(selectAllProductsQuery)
        ) {
            while (selectResultSet.next()) {
                Product product = new Product(
                        selectResultSet.getLong("ID"),
                        selectResultSet.getString("NAME"),
                        selectResultSet.getDouble("PRICE"),
                        selectResultSet.getString("IMAGE"),
                        selectResultSet.getBoolean("ACTIVE")
                );

                products.add(product);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return products;
    }

    @Override
    public Page<Product> selectPageProducts(Page<Product> page) {
        return getPageProducts(SELECT_PAGE_PRODUCTS_QUERY, COUNT_PAGE_PRODUCTS_QUERY, page);
    }

    @Override
    public Page<Product> selectPageActiveProducts(Page<Product> page) {
        return getPageProducts(SELECT_PAGE_ACTIVE_PRODUCTS_QUERY, COUNT_PAGE_ACTIVE_PRODUCTS_QUERY, page);
    }

    private Page<Product> getPageProducts(String selectPageProductsQuery, String countPageProductsQuery, Page<Product> page) {
        List<Object> parameters = Arrays.asList(
                page.getFilter().getName(),
                page.getPageSize(),
                page.getOffset()
        );

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = getPreparedStatement(connection, setSortAndDirection(selectPageProductsQuery, page.getSortBy(), page.getDirection()), parameters);
             ResultSet selectResultSet = preparedStatement.executeQuery();
             PreparedStatement countPreparedStatement = getPreparedStatement(connection, setSortAndDirection(countPageProductsQuery, page.getSortBy(), page.getDirection()), Collections.singletonList(page.getFilter().getName()));
             ResultSet countResultSet = countPreparedStatement.executeQuery()
        ) {
            List<Product> products = new ArrayList<>();
            while (selectResultSet.next()) {
                Product product = new Product(
                        selectResultSet.getLong("ID"),
                        selectResultSet.getString("NAME"),
                        selectResultSet.getDouble("PRICE"),
                        selectResultSet.getString("IMAGE"),
                        selectResultSet.getBoolean("ACTIVE")
                );
                products.add(product);
            }
            if(countResultSet.next()) {
                page.setTotalElements(countResultSet.getLong(1));
            }
            page.setElements(products);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return page;
    }

    @Override
    public Product insertProduct(Product product) throws DaoException {
        List<Object> parameters = Arrays.asList(
                product.getName(),
                product.getPrice(),
                product.getImage(),
                product.isActive()
        );

        try (Connection connection = getConnection();
             PreparedStatement selectNamePreparedStatement = getPreparedStatement(connection, SELECT_PRODUCT_BY_NAME_QUERY, Collections.singletonList(product.getName()));
             ResultSet selectNameResultSet = selectNamePreparedStatement.executeQuery();
             PreparedStatement insertPreparedStatement = getPreparedStatement(connection, INSERT_PRODUCT_QUERY, parameters)
        ) {
            if (!selectNameResultSet.next()) {
                try (ResultSet insertResultSet = insertPreparedStatement.executeQuery()) {
                    if (insertResultSet.next()) {
                        product.setId(insertResultSet.getLong("ID"));
                    }
                }
            } else {
                throw new DaoException("Product with this name does already exist.");
            }
        } catch (
                SQLException exception) {
            throw new RuntimeException(exception);
        }

        return product;
    }

    @Override
    public Product updateProduct(Product product) throws DaoException {
        List<Object> parameters = Arrays.asList(
                product.getPrice(),
                product.getImage(),
                product.isActive(),
                product.getId()
        );

        try (Connection connection = getConnection();
             PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_PRODUCT_BY_ID_QUERY, Collections.singletonList(product.getId()));
//             PreparedStatement selectNamePreparedStatement = getPreparedStatement(connection, SELECT_PRODUCT_BY_NAME_QUERY, Collections.singletonList(product.getName()));
             ResultSet selectResultSet = selectPreparedStatement.executeQuery();
//             ResultSet selectNameResultSet = selectNamePreparedStatement.executeQuery();
             PreparedStatement updatePreparedStatement = getPreparedStatement(connection, UPDATE_PRODUCT_QUERY, parameters)
        ) {
            if (selectResultSet.next()) {
//                if (!selectNameResultSet.next()) {
                    updatePreparedStatement.execute();
//                } else {
//                    throw new DaoException("Product with this name does already exist.");
//                }
            } else {
                throw new DaoException("Product with this id does not exist.");
            }
        } catch (
                SQLException exception) {
            throw new RuntimeException(exception);
        }

        return product;
    }
}
