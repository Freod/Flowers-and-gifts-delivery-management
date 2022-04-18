package com.flowersAndGifts.dao.impl;

import com.flowersAndGifts.dao.UserDao;
import com.flowersAndGifts.exception.DaoException;
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
    private static final String INSERT_USER_QUERY = "INSERT INTO USERS (email, password, firstname, lastname, role, active) VALUES(?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String UPDATE_USER_PASSWORD_BY_EMAIL_QUERY = "UPDATE USERS SET password = ? WHERE email = ?";
    private static final String UPDATE_USER_ACTIVE_BY_EMAIL_QUERY = "UPDATE USERS SET active = ? WHERE email = ?";
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
        try (Connection connection = getConnection()) {
            try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, selectUserQuery, parameter)) {
                try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
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

        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet selectResultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY)) {
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
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return users;
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

        try (Connection connection = getConnection()) {
            try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_USER_BY_EMAIL_QUERY, Collections.singletonList(user.getEmail()))) {
                try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
                    try (PreparedStatement insertPreparedStatement = getPreparedStatement(connection, INSERT_USER_QUERY, parameters)) {
                        if (!selectResultSet.next()) {
                            try (ResultSet insertResultSet = insertPreparedStatement.executeQuery()) {
                                if (insertResultSet.next()) {
                                    user.setId(insertResultSet.getLong("ID"));
                                }
                            }
                        } else {
                            throw new DaoException("User with this email does already exist.");
                        }
                    }
                }
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

    private User updateUser(String updateUserQuery, List<Object> parameters) throws DaoException {
        User user;

        try (Connection connection = getConnection()) {
            try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_USER_BY_EMAIL_QUERY, Collections.singletonList(parameters.get(1)))) {
                try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
                    try (PreparedStatement updatePreparedStatement = getPreparedStatement(connection, updateUserQuery, parameters)) {
                        if (selectResultSet.next()) {
                            updatePreparedStatement.execute();
                            user = selectUserByEmail((String) parameters.get(1));
                        } else {
                            throw new DaoException("User with this email does not exist.");
                        }
                    }
                }
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
        try (Connection connection = getConnection()) {
            try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, selectUserQuery, parameter)) {
                try (ResultSet resultSet = selectPreparedStatement.executeQuery()) {
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
                }
            }
        } catch (
                SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}