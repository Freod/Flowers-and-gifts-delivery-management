package com.flowersAndGifts.dao;

import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Product;

import java.util.List;

public interface ProductDao {
    Product selectProductById(Long id) throws DaoException;

    Product selectProductByName(String name) throws DaoException;

    List<Product> selectAllProducts();

    List<Product> selectAllActiveProducts();

    Page<Product> selectPageProducts(Page<Product> page);

    Page<Product> selectPageActiveProducts(Page<Product> page);

    Product insertProduct(Product product) throws DaoException;

    Product updateProduct(Product product) throws DaoException;
}
