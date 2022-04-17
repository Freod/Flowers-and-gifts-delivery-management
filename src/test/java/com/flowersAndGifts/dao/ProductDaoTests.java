package com.flowersAndGifts.dao;

import com.flowersAndGifts.dao.impl.ProductDaoImpl;
import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Product;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ProductDaoTests {
    private ProductDao productDao;
    private final Product product = new Product("TestName", 20.0, "TestImage.png", true);

    @Before
    public void setUp(){
        //TODO:TRANSACTION
        productDao = new ProductDaoImpl();
    }

    @Test
    public void testProductDao() throws Exception{
        //TODO:TESTS
        List<Product> expectedList = productDao.selectAllProducts();
        Product dbProduct = productDao.insertProduct(product);
        List<Product> selectList1 = productDao.selectAllProducts();

        assertNotEquals(expectedList, selectList1);
        assertNotNull(dbProduct.getId());

        try{
            productDao.insertProduct(product);
        }
        catch (Exception e){
            assertTrue(e.getClass().equals(DaoException.class));
            assertTrue(e.getMessage().equals("Product with this name does already exist."));
        }

        List<Product> selectList2 = productDao.selectAllProducts();

        assertEquals(selectList1, selectList2);

        Product product1 = productDao.selectProductById(dbProduct.getId());

        assertEquals(dbProduct, product1);

        try {
            productDao.selectProductById(0L);
        }catch (Exception e){
            assertTrue(e.getClass().equals(DaoException.class));
            assertTrue(e.getMessage().equals("Product with this id does not exist."));
        }

        Product product2 = productDao.selectProductByName(dbProduct.getName());

        assertEquals(dbProduct, product2);

        List<Product> productList1 = productDao.selectAllProducts();
        List<Product> productList2 = productDao.selectAllActiveProducts();

        assertEquals(productList1, productList2);

        product2.setActive(false);
        product2.setName("test2");

        assertNotEquals(dbProduct, product2);

        productDao.updateProduct(product2);
        Product product3 = productDao.selectProductByName(product2.getName());

        assertNotEquals(dbProduct, product3);
        assertEquals(product2, product3);

        List<Product> productList3 = productDao.selectAllProducts();
        List<Product> productList4 = productDao.selectAllActiveProducts();
        assertNotEquals(productList3, productList4);

        try {
            productDao.selectProductByName(dbProduct.getName());
        }catch (Exception e){
            assertTrue(e.getClass().equals(DaoException.class));
            assertTrue(e.getMessage().equals("Product with this name does not exist."));
        }

        Product product4 = productDao.insertProduct(product);
        Product product5 = productDao.selectProductByName(product4.getName());
        product5.setName(product3.getName());
        try{
            productDao.updateProduct(product5);
        }catch (Exception e){
            assertTrue(e.getClass().equals(DaoException.class));
            assertTrue(e.getMessage().equals("Product with this name does already exist."));
        }
    }

    @Before
    public void endTests(){
        //TODO:ROLLBACK
        System.out.println(":)");
    }
}
