package com.flowersAndGifts.dao.impl;

import com.flowersAndGifts.dao.UserDao;
import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserDaoImpl extends AbstractDao implements UserDao {

    private static final String SELECT_USER_BY_ID_QUERY = "SELECT u.id, u.email, u.firstname, u.lastname, u.role, u.active FROM USERS u WHERE u.id = ?";
    private static final String SELECT_USER_BY_EMAIL_QUERY = "SELECT u.id, u.email, u.firstname, u.lastname, u.role, u.active FROM USERS u WHERE u.email = ?";
    private static final String SELECT_USER_BY_EMAIL_AND_PASSWORD_QUERY = "SELECT u.id, u.email, u.firstname, u.lastname, u.role, u.active FROM USERS u WHERE u.email = ? AND u.password = ?";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT u.id, u.email, u.firstname, u.lastname, u.role, u.active FROM USERS u";
    private static final String SELECT_PAGE_USERS_QUERY = "SELECT u.id, u.email, u.firstname, u.lastname, u.role, u.active FROM USERS u" +
            " WHERE UPPER(u.email) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(u.firstname) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(u.lastname) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(u.role) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " ORDER BY u.%s %s LIMIT ? OFFSET ?";
    private static final String COUNT_PAGE_USERS_QUERY = "SELECT COUNT(u.id) FROM USERS u" +
            " WHERE UPPER(u.email) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(u.firstname) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(u.lastname) LIKE CONCAT('%%', UPPER(?), '%%')" +
            " AND UPPER(u.role) LIKE CONCAT('%%', UPPER(?), '%%')";
    private static final String INSERT_USER_QUERY = "INSERT INTO USERS (email, password, firstname, lastname, role, active) VALUES(?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String UPDATE_USER_PASSWORD_BY_EMAIL_QUERY = "UPDATE USERS SET password = ? WHERE email = ?";
    private static final String UPDATE_USER_ACTIVE_BY_EMAIL_QUERY = "UPDATE USERS SET active = ? WHERE email = ?";
    private static final String UPDATE_USER_ROLE_BY_EMAIL_QUERY = "UPDATE USERS SET role = ? WHERE email = ?";
    private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM USERS WHERE id = ?";
    private static final String DELETE_USER_BY_EMAIL_QUERY = "DELETE FROM USERS WHERE email = ?";

    @Override
    public User selectUserById(Long id) throws DaoException {
        return getUser(SELECT_USER_BY_ID_QUERY, Collections.singletonList(id));
    }

    @Override
    public User selectUserByEmail(String email) throws DaoException {
        return getUser(SELECT_USER_BY_EMAIL_QUERY, Collections.singletonList(email));
    }

    @Override
    public User selectUserByEmailAndPassword(String email, String password) throws DaoException {
        return getUser(SELECT_USER_BY_EMAIL_AND_PASSWORD_QUERY, Arrays.asList(email, password));
    }

    private User getUser(String selectUserQuery, List<Object> parameter) throws DaoException {
        User user;
        try (Connection connection = getConnection();
             PreparedStatement selectPreparedStatement = getPreparedStatement(connection, selectUserQuery, parameter);
             ResultSet selectResultSet = selectPreparedStatement.executeQuery()
        ) {
            if (selectResultSet.next()) {
                user = new User(
                        selectResultSet.getLong("ID"),
                        selectResultSet.getString("EMAIL"),
                        selectResultSet.getString("FIRSTNAME"),
                        selectResultSet.getString("LASTNAME"),
                        Role.valueOf(selectResultSet.getString("ROLE")),
                        selectResultSet.getBoolean("ACTIVE")
                );
            } else {
                if (parameter.get(0).getClass() == Long.class) {
                    throw new DaoException("User with this id does not exist.");
                } else {
                    throw new DaoException("User with this email does not exist.");
                }

            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet selectResultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY)
        ) {
            while (selectResultSet.next()) {
                User user = new User(
                        selectResultSet.getLong("ID"),
                        selectResultSet.getString("EMAIL"),
                        selectResultSet.getString("FIRSTNAME"),
                        selectResultSet.getString("LASTNAME"),
                        Role.valueOf(selectResultSet.getString("ROLE")),
                        selectResultSet.getBoolean("ACTIVE")
                );
                users.add(user);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return users;
    }

    @Override
    public Page<User> selectPageUsers(Page<User> page) {
        List<Object> parameters = Arrays.asList(
                page.getFilter().getEmail(),
                page.getFilter().getFirstname(),
                page.getFilter().getLastname(),
                page.getFilter().getRole(),
                page.getPageSize(),
                page.getOffset()
        );

        List<Object> parameters2 = Arrays.asList(
                page.getFilter().getEmail(),
                page.getFilter().getFirstname(),
                page.getFilter().getLastname(),
                page.getFilter().getRole()
                );

        try (Connection connection = getConnection();
             PreparedStatement selectPreparedStatement = getPreparedStatement(connection, setSortAndDirection(SELECT_PAGE_USERS_QUERY, page.getSortBy(), page.getDirection()), parameters);
             ResultSet selectResultSet = selectPreparedStatement.executeQuery();
             PreparedStatement countPreparedStatement = getPreparedStatement(connection, setSortAndDirection(COUNT_PAGE_USERS_QUERY, page.getSortBy(), page.getDirection()), parameters2);
             ResultSet countResultSet = countPreparedStatement.executeQuery()
        ) {
            List<User> users = new ArrayList<>();
            while (selectResultSet.next()) {
                User user = new User(
                        selectResultSet.getLong("ID"),
                        selectResultSet.getString("EMAIL"),
                        selectResultSet.getString("FIRSTNAME"),
                        selectResultSet.getString("LASTNAME"),
                        Role.valueOf(selectResultSet.getString("ROLE")),
                        selectResultSet.getBoolean("ACTIVE")
                );
                users.add(user);
            }
            if(countResultSet.next()){
                page.setTotalElements(countResultSet.getLong(1));
            }
            page.setElements(users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return page;
    }

    @Override
    public User insertUser(User user) throws DaoException {
        List<Object> parameters = Arrays.asList(
                user.getEmail(),
                user.getPassword(),
                user.getFirstname(),
                user.getLastname(),
                user.getRole().toString(),
                user.getActive()
        );

        try (Connection connection = getConnection();
             PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_USER_BY_EMAIL_QUERY, Collections.singletonList(user.getEmail()));
             ResultSet selectResultSet = selectPreparedStatement.executeQuery();
             PreparedStatement insertPreparedStatement = getPreparedStatement(connection, INSERT_USER_QUERY, parameters)
        ) {
            if (!selectResultSet.next()) {
                try (ResultSet insertResultSet = insertPreparedStatement.executeQuery()) {
                    if (insertResultSet.next()) {
                        user.setId(insertResultSet.getLong("ID"));
                    }
                }
            } else {
                throw new DaoException("User with this email does already exist.");
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return user;
    }

    @Override
    public User updateUserPassword(User user) throws DaoException {
        return updateUser(UPDATE_USER_PASSWORD_BY_EMAIL_QUERY, Arrays.asList(user.getPassword(), user.getEmail()));
    }

    @Override
    public User updateUserActive(User user) throws DaoException {
        return updateUser(UPDATE_USER_ACTIVE_BY_EMAIL_QUERY, Arrays.asList(user.getActive(), user.getEmail()));
    }

    @Override
    public User updateUserRole(User user) throws DaoException {
        return updateUser(UPDATE_USER_ROLE_BY_EMAIL_QUERY, Arrays.asList(user.getRole().toString(), user.getEmail()));
    }

    private User updateUser(String updateUserQuery, List<Object> parameters) throws DaoException {
        User user;

        try (Connection connection = getConnection();
             PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_USER_BY_EMAIL_QUERY, Collections.singletonList(parameters.get(1)));
             ResultSet selectResultSet = selectPreparedStatement.executeQuery();
             PreparedStatement updatePreparedStatement = getPreparedStatement(connection, updateUserQuery, parameters)
        ) {
            if (selectResultSet.next()) {
                updatePreparedStatement.execute();
                user = selectUserByEmail((String) parameters.get(1));
            } else {
                throw new DaoException("User with this email does not exist.");
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return user;
    }

    @Override
    public void deleteUserById(Long id) throws DaoException {
        deleteUser(SELECT_USER_BY_ID_QUERY, DELETE_USER_BY_ID_QUERY, Collections.singletonList(id));
    }

    @Override
    public void deleteUserByEmail(String email) throws DaoException {
        deleteUser(SELECT_USER_BY_EMAIL_QUERY, DELETE_USER_BY_EMAIL_QUERY, Collections.singletonList(email));
    }

    private void deleteUser(String selectUserQuery, String deleteUserQuery, List<Object> parameter) throws DaoException {
        try (Connection connection = getConnection();
             PreparedStatement selectPreparedStatement = getPreparedStatement(connection, selectUserQuery, parameter);
             ResultSet resultSet = selectPreparedStatement.executeQuery()
        ) {
            if (resultSet.next()) {
                try (PreparedStatement deletePreparedStatement = getPreparedStatement(connection, deleteUserQuery, parameter)) {
                    deletePreparedStatement.execute();
                }
            } else {
                if (parameter.get(0).getClass() == Long.class) {
                    throw new DaoException("User with this id does not exist.");
                } else {
                    throw new DaoException("User with this email does not exist.");
                }
            }
        } catch (
                SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}