package com.flowersAndGifts.dao;

import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Product;

import java.util.List;

public interface ProductDao {
    Product selectProductById(Long id) throws DaoException;

    Product selectProductByName(String name) throws DaoException;

    List<Product> selectAllProducts() throws DaoException;

    List<Product> selectAllActiveProducts() throws DaoException;

    Page<Product> selectPageProducts(Page<Product> page) throws DaoException;

    Page<Product> selectPageActiveProducts(Page<Product> page) throws DaoException;

    Product insertProduct(Product product) throws DaoException;

    Product updateProduct(Product product) throws DaoException;
}
