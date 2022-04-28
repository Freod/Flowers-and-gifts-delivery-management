package com.flowersAndGifts.service.impl;

import com.flowersAndGifts.dao.DaoFactory;
import com.flowersAndGifts.dao.ProductDao;
import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Product;
import com.flowersAndGifts.service.ProductService;

public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao = DaoFactory.getInstance().getProductDao();

    @Override
    public Product takeProduct(Product product) throws ServiceException {
        try {
            return productDao.selectProductById(product.getId());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Product addProduct(Product product) throws ServiceException {
        try {
            return productDao.insertProduct(product);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Product updateProduct(Product product) throws ServiceException {
        try {
            return productDao.updateProduct(product);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Page<Product> allProductsByPage(Page<Product> page) throws ServiceException {
        try {
            return productDao.selectPageProducts(page);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Page<Product> allActiveProductsByPage(Page<Product> page) throws ServiceException {
        try {
            return productDao.selectPageActiveProducts(page);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}