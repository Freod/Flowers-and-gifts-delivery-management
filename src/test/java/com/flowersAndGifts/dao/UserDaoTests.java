package com.flowersAndGifts.dao;

import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserDaoTests {
    private UserDao userDao;
    private final User user = new User("test", "test", "test", "test", Role.CUSTOMER);

    @Test
    public void testInsertDeleteUser() throws Exception {
        userDao = new UserDaoImpl(false);

        List<User> expectedList1 = userDao.selectAllUsers();
        User insertedUser = userDao.insertUser(user);
        List<User> actualList1 = userDao.selectAllUsers();

        assertEquals(user.getEmail(), insertedUser.getEmail());
        assertNotEquals(expectedList1, actualList1);

        try {
            userDao.insertUser(user);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("User with this email already exist."));
            assertEquals(e.getClass(), DaoException.class);
        }

        List<User> expectedList2 = userDao.selectAllUsers();
        List<User> actualList2 = userDao.selectAllUsers();

        assertNotEquals(expectedList1, actualList2);
        assertEquals(expectedList2, actualList2);

        userDao.deleteUserById(insertedUser.getId());

        List<User> actualList3 = userDao.selectAllUsers();
        assertEquals(expectedList1, actualList3);
        assertNotEquals(expectedList2, actualList3);

        try {
            userDao.deleteUserById(insertedUser.getId());
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("User with this id not exist."));
            assertEquals(e.getClass(), DaoException.class);
        }

        List<User> actualList4 = userDao.selectAllUsers();
        assertEquals(expectedList1, actualList4);
        assertNotEquals(expectedList2, actualList4);

        userDao.rollback();
    }

    @Test
    public void testUpdateUser() throws Exception {
        userDao = new UserDaoImpl(false);

        List<User> expectedList = userDao.selectAllUsers();
        User insertedUser = userDao.insertUser(user);
        User expected1 = insertedUser;
        List<User> actualList = userDao.selectAllUsers();

        assertNotEquals(expectedList, actualList);
        assertEquals(user, insertedUser);

        User actual1 = userDao.selectUserByEmailAndPassword(user.getEmail(), user.getPassword());
        User expected2 = actual1;
        assertEquals(expected1.getId(), actual1.getId());

        insertedUser.setPassword("test2");
        userDao.updateUserPassword(insertedUser);

        User actual2 = userDao.selectUserByEmailAndPassword(insertedUser.getEmail(), "test2");

        assertEquals(expected1.getId(), actual2.getId());
        assertEquals(expected2.getId(), actual2.getId());

        try {
            userDao.selectUserByEmailAndPassword(actual1.getEmail(), "test");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("User with this email and password not exist."));
            assertEquals(e.getClass(), DaoException.class);
        }

        List<User> expectedList2 = userDao.selectAllUsers();
        userDao.deleteUserById(insertedUser.getId());
        actualList = userDao.selectAllUsers();
        assertEquals(expectedList, actualList);
        assertNotEquals(expectedList2, actualList);

        insertedUser.setPassword("test");
        try {
            userDao.updateUserPassword(insertedUser);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("User with this email not exist."));
            assertEquals(e.getClass(), DaoException.class);
        }

        userDao.rollback();
    }

    @Test
    public void testSelectUser() throws Exception {
        userDao = new UserDaoImpl(false);

        List<User> expectedList = userDao.selectAllUsers();

        User insertedUser = userDao.insertUser(user);

        List<User> actualList = userDao.selectAllUsers();

        assertNotEquals(expectedList, actualList);

        User actual1 = userDao.selectUserById(insertedUser.getId());
        User actual2 = userDao.selectUserByEmail(insertedUser.getEmail());
        User actual3 = userDao.selectUserByEmailAndPassword(insertedUser.getEmail(), insertedUser.getPassword());

        assertEquals(insertedUser.getId(), actual1.getId());
        assertEquals(insertedUser.getEmail(), actual1.getEmail());
        assertEquals(insertedUser.getId(), actual2.getId());
        assertEquals(insertedUser.getEmail(), actual2.getEmail());
        assertEquals(insertedUser.getId(), actual3.getId());
        assertEquals(insertedUser.getEmail(), actual3.getEmail());

        userDao.deleteUserById(insertedUser.getId());

        actualList = userDao.selectAllUsers();
        assertEquals(expectedList, actualList);

        try {
            userDao.selectUserById(insertedUser.getId());
        } catch (Exception e) {
            assertEquals(e.getClass(), DaoException.class);
            assertTrue(e.getMessage().contains("User with this id not exist."));
        }
        try {
            userDao.selectUserByEmail(insertedUser.getEmail());
        } catch (Exception e) {
            assertEquals(e.getClass(), DaoException.class);
            assertTrue(e.getMessage().contains("User with this email not exist."));
        }
        try {
            userDao.selectUserByEmailAndPassword(insertedUser.getEmail(), insertedUser.getPassword());
        } catch (Exception e) {
            assertEquals(e.getClass(), DaoException.class);
            assertTrue(e.getMessage().contains("User with this email and password not exist."));
        }

        userDao.rollback();
    }
}
