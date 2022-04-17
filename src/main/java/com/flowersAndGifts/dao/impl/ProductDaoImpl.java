package com.flowersAndGifts.dao.impl;

import com.flowersAndGifts.dao.ProductDao;
import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductDaoImpl extends AbstractDao implements ProductDao {

    private static final String SELECT_PRODUCT_BY_ID_QUERY = "SELECT p.id, p.name, p.price, p.image, p.active FROM PRODUCTS p WHERE id = ?";
    private static final String SELECT_PRODUCT_BY_NAME_QUERY = "SELECT p.id, p.name, p.price, p.image, p.active FROM PRODUCTS p WHERE name = ?";
    private static final String SELECT_ALL_PRODUCTS_QUERY = "SELECT p.id, p.name, p.price, p.image, p.active FROM PRODUCTS p";
    private static final String SELECT_ALL_ACTIVE_PRODUCTS_QUERY = "SELECT p.id, p.name, p.price, p.image, p.active FROM PRODUCTS p where p.active = true";
    private static final String INSERT_PRODUCT_QUERY = "INSERT INTO PRODUCTS (name, price, image, active) VALUES(?, ?, ?, ?) RETURNING id";
    private static final String UPDATE_PRODUCT_QUERY = "UPDATE PRODUCTS SET name = ?, price = ?, image = ?, active = ? WHERE id = ?";

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

        try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, selectProductQuery, parameter)) {
            try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
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
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return product;
    }

    @Override
    public List<Product> selectAllProducts() throws DaoException {
        return getAllProducts(SELECT_ALL_PRODUCTS_QUERY);
    }

    @Override
    public List<Product> selectAllActiveProducts() throws DaoException {
        return getAllProducts(SELECT_ALL_ACTIVE_PRODUCTS_QUERY);
    }

    private List<Product> getAllProducts(String selectAllProductsQuery) {
        List<Product> products = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            try (ResultSet selectResultSet = statement.executeQuery(selectAllProductsQuery)) {
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
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return products;
    }

    @Override
    public Product insertProduct(Product product) throws DaoException {
        List<Object> parameters = Arrays.asList(
                product.getName(),
                product.getPrice(),
                product.getImage(),
                product.isActive()
        );

        try (PreparedStatement selectNamePreparedStatement = getPreparedStatement(connection, SELECT_PRODUCT_BY_NAME_QUERY, Collections.singletonList(product.getName()))) {
            try (ResultSet selectNameResultSet = selectNamePreparedStatement.executeQuery()) {
                try (PreparedStatement insertPreparedStatement = getPreparedStatement(connection, INSERT_PRODUCT_QUERY, parameters)) {
                    if (!selectNameResultSet.next()) {
                        try (ResultSet insertResultSet = insertPreparedStatement.executeQuery()) {
                            if (insertResultSet.next()) {
                                product.setId(insertResultSet.getLong("ID"));
                            }
                        }
                    } else {
                        throw new DaoException("Product with this name does already exist.");
                    }
                }
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
                product.getName(),
                product.getPrice(),
                product.getImage(),
                product.isActive(),
                product.getId()
        );

        try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_PRODUCT_BY_ID_QUERY, Collections.singletonList(product.getId()))) {
            try (PreparedStatement selectNamePreparedStatement = getPreparedStatement(connection, SELECT_PRODUCT_BY_NAME_QUERY, Collections.singletonList(product.getName()))) {
                try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
                    try (ResultSet selectNameResultSet = selectNamePreparedStatement.executeQuery()) {
                        try (PreparedStatement updatePreparedStatement = getPreparedStatement(connection, UPDATE_PRODUCT_QUERY, parameters)) {
                            if (selectResultSet.next()) {
                                if(!selectNameResultSet.next()) {
                                    updatePreparedStatement.execute();
                                }
                                else{
                                    throw new DaoException("Product with this name does already exist.");
                                }
                            } else {
                                throw new DaoException("Product with this id does not exist.");
                            }
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return product;
    }
}
