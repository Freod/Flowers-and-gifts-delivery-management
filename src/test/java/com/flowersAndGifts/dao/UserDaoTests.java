package com.flowersAndGifts.dao;

import com.flowersAndGifts.dao.impl.UserDaoImpl;
import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserDaoTests {
    private UserDao userDao;
    private final User user = new User("test", "test", "test", "test", Role.CUSTOMER, true);

    @Before
    public void setUp(){
        //TODO:TRANSACTION
        userDao = new UserDaoImpl();
    }
    
    @Test
    public void testUserDao() throws Exception {
        List<User> expectedList1 = userDao.selectAllUsers();
        User user1 = userDao.insertUser(user);
        List<User> actualList1 = userDao.selectAllUsers();

        assertNotEquals(expectedList1, actualList1);
        assertNotNull(user1.getId());
        assertTrue(user.equals(user1));

        try {
            userDao.insertUser(user);
        } catch (Exception e) {
            assertTrue(e.getClass().equals(DaoException.class));
            assertTrue(e.getMessage().equals("User with this email does already exist."));
        }

        User user2 = userDao.selectUserById(user1.getId());
        assertFalse(user1.equals(user2));
        assertEquals(user1.getId(), user2.getId());
        User user3 = userDao.selectUserByEmail(user1.getEmail());
        assertTrue(user2.equals(user3));
        User user4 = userDao.selectUserByEmailAndPassword(user1.getEmail(), user.getPassword());
        assertTrue(user2.equals(user4));

        user2.setPassword("test2");
        userDao.updateUserPassword(user2);
        User user5 = userDao.selectUserByEmailAndPassword(user2.getEmail(), user2.getPassword());
        assertTrue(user3.equals(user5));
        user5.setActive(false);
        userDao.updateUserActive(user5);
        User user6 = userDao.selectUserByEmail(user1.getEmail());
        assertTrue(user5.equals(user6));

        userDao.deleteUserById(user6.getId());
        List<User> actualList2 = userDao.selectAllUsers();
        assertEquals(expectedList1, actualList2);

        try {
            userDao.deleteUserById(user6.getId());
        }catch (Exception e){
            assertTrue(e.getClass().equals(DaoException.class));
            assertTrue(e.getMessage().equals("User with this id does not exist."));
        }

        try {
            userDao.selectUserById(user1.getId());
        }catch (Exception e){
            assertTrue(e.getClass().equals(DaoException.class));
            assertTrue(e.getMessage().equals("User with this id does not exist."));
        }

        List<User> expectedList2 = userDao.selectAllUsers();
        userDao.insertUser(user);
        userDao.deleteUserByEmail(user.getEmail());
        List<User> actualList3 = userDao.selectAllUsers();
        assertEquals(expectedList2, actualList3);

        try {
            userDao.deleteUserByEmail(user.getEmail());
        }catch (Exception e){
            assertTrue(e.getClass().equals(DaoException.class));
            assertTrue(e.getMessage().equals("User with this email does not exist."));
        }

        try{
            userDao.selectUserByEmail(user1.getEmail());
        }catch (Exception e){
            assertTrue(e.getClass().equals(DaoException.class));
            assertTrue(e.getMessage().equals("User with this email does not exist."));
        }

        try {
            userDao.selectUserByEmailAndPassword(user1.getEmail(), user.getPassword());
        }catch (Exception e){
            assertTrue(e.getClass().equals(DaoException.class));
            assertTrue(e.getMessage().equals("User with this email does not exist."));
        }
    }

    @After
    public void endTests(){
        //TODO:ROLLBACK
        System.out.println(":)");
    }
}
