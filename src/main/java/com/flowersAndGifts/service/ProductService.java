package com.flowersAndGifts.service;

import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Product;

public interface ProductService {
    Product takeProduct(Product product) throws ServiceException;

    Product addProduct(Product product) throws ServiceException;

    Product updateProduct(Product product) throws ServiceException;

    Page<Product> allProductsByPage(Page<Product> page) throws ServiceException;

    Page<Product> allActiveProductsByPage(Page<Product> page) throws ServiceException;
}