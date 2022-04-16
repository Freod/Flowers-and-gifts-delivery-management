package com.flowersAndGifts.dao;

import com.flowersAndGifts.exception.DaoException;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserDaoImpl extends AbstractDao implements UserDao {
    Connection connection;
    private boolean autoCommitStatus = true;

    private static final String SELECT_USER_QUERY_BY_ID = "SELECT u.id, u.email, u.firstname, u.lastname, u.role FROM USERS u WHERE u.id = ?";
    private static final String SELECT_USER_QUERY_BY_EMAIL = "SELECT u.id, u.email, u.firstname, u.lastname, u.role FROM USERS u WHERE u.email = ?";
    private static final String SELECT_USER_QUERY_BY_EMAIL_AND_PASSWORD = "SELECT u.id, u.email, u.firstname, u.lastname, u.role FROM USERS u WHERE u.email = ? AND u.password = ?";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT u.id, u.email, u.firstname, u.lastname, u.role FROM USERS u";
    private static final String INSERT_USER_QUERY = "INSERT INTO USERS (email, password, firstname, lastname, role) VALUES(?, ?, ?, ?, ?) RETURNING id";
    private static final String UPDATE_USER_PASSWORD_QUERY_BY_EMAIL = "UPDATE USERS SET password = ? WHERE email = ?";
    private static final String DELETE_USER_QUERY_BY_ID = "DELETE FROM USERS WHERE id = ?";

    public UserDaoImpl() {
    }

    public UserDaoImpl(boolean autoCommitStatus) throws DaoException {
        this.autoCommitStatus = autoCommitStatus;
        connection = getConnection(autoCommitStatus);
    }

    @Override
    public User selectUserById(Long id) throws DaoException {
        User user;

        if (autoCommitStatus) {
            connection = getConnection(autoCommitStatus);
        }
        try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_USER_QUERY_BY_ID, Collections.singletonList(id))) {
            try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
                if (selectResultSet.next()) {
                    user = new User(
                            selectResultSet.getLong("ID"),
                            selectResultSet.getString("EMAIL"),
                            selectResultSet.getString("FIRSTNAME"),
                            selectResultSet.getString("LASTNAME"),
                            Role.valueOf(selectResultSet.getString("ROLE"))
                    );
                } else {
                    throw new DaoException("User with this id not exist.");
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (autoCommitStatus) {
                closeConnection(connection);
            }
        }

        return user;
    }

    @Override
    public User selectUserByEmail(String email) throws DaoException {
        User user;

        if (autoCommitStatus) {
            connection = getConnection(autoCommitStatus);
        }
        try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_USER_QUERY_BY_EMAIL, Collections.singletonList(email))) {
            try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
                if (selectResultSet.next()) {
                    user = new User(
                            selectResultSet.getLong("ID"),
                            selectResultSet.getString("EMAIL"),
                            selectResultSet.getString("FIRSTNAME"),
                            selectResultSet.getString("LASTNAME"),
                            Role.valueOf(selectResultSet.getString("ROLE"))
                    );
                } else {
                    throw new DaoException("User with this email not exist.");
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (autoCommitStatus) {
                closeConnection(connection);
            }
        }

        return user;
    }

    @Override
    public User selectUserByEmailAndPassword(String email, String password) throws DaoException {
        User user;

        if (autoCommitStatus) {
            connection = getConnection(autoCommitStatus);
        }
        try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_USER_QUERY_BY_EMAIL_AND_PASSWORD, Arrays.asList(email, password))) {
            try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
                if (selectResultSet.next()) {
                    user = new User(
                            selectResultSet.getLong("ID"),
                            selectResultSet.getString("EMAIL"),
                            selectResultSet.getString("FIRSTNAME"),
                            selectResultSet.getString("LASTNAME"),
                            Role.valueOf(selectResultSet.getString("ROLE"))
                    );
                } else {
                    throw new DaoException("User with this email and password not exist.");
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (autoCommitStatus) {
                closeConnection(connection);
            }
        }

        return user;
    }

    @Override
    public List<User> selectAllUsers() throws DaoException {
        List<User> users = new ArrayList<>();

        if (autoCommitStatus) {
            connection = getConnection(autoCommitStatus);
        }
        try (Statement statement = connection.createStatement()) {
            try (ResultSet selectResultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY)) {
                while (selectResultSet.next()) {
                    User user = new User(
                            selectResultSet.getLong("ID"),
                            selectResultSet.getString("EMAIL"),
                            selectResultSet.getString("FIRSTNAME"),
                            selectResultSet.getString("LASTNAME"),
                            Role.valueOf(selectResultSet.getString("ROLE"))
                    );

                    users.add(user);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (autoCommitStatus) {
                closeConnection(connection);
            }
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
                user.getRole().toString()
        );

        if (autoCommitStatus) {
            connection = getConnection(autoCommitStatus);
        }
        try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_USER_QUERY_BY_EMAIL, Collections.singletonList(user.getEmail()))) {
            try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
                PreparedStatement insertPreparedStatement = getPreparedStatement(connection, INSERT_USER_QUERY, parameters);
                if (!selectResultSet.next()) {
                    try (ResultSet insertResultSet = insertPreparedStatement.executeQuery()) {
                        if (insertResultSet.next()) {
                            user.setId(insertResultSet.getLong("ID"));
                        }
                    }
                } else {
                    throw new DaoException("User with this email already exist.");
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (autoCommitStatus) {
                closeConnection(connection);
            }
        }

        return user;
    }

    @Override
    public User updateUserPassword(User user) throws DaoException {

        if (autoCommitStatus) {
            connection = getConnection(autoCommitStatus);
        }
        try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_USER_QUERY_BY_EMAIL, Collections.singletonList(user.getEmail()))) {
            try (ResultSet selectResultSet = selectPreparedStatement.executeQuery()) {
                PreparedStatement updatePreparedStatement = getPreparedStatement(connection, UPDATE_USER_PASSWORD_QUERY_BY_EMAIL, Arrays.asList(user.getPassword(), user.getEmail()));
                if (selectResultSet.next()) {
                    updatePreparedStatement.execute();
                    user.setPassword(null);
                } else {
                    throw new DaoException("User with this email not exist.");
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (autoCommitStatus) {
                closeConnection(connection);
            }
        }

        return user;
    }

    @Override
    public void deleteUserById(Long id) throws DaoException {
        List<Object> parameters = Collections.singletonList(id);

        if (autoCommitStatus) {
            connection = getConnection(autoCommitStatus);
        }
        try (PreparedStatement selectPreparedStatement = getPreparedStatement(connection, SELECT_USER_QUERY_BY_ID, parameters)) {
            try (ResultSet resultSet = selectPreparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    try (PreparedStatement deletePreparedStatement = getPreparedStatement(connection, DELETE_USER_QUERY_BY_ID, parameters)) {
                        deletePreparedStatement.execute();
                    }
                } else {
                    throw new DaoException("User with this id not exist.");
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (autoCommitStatus) {
                closeConnection(connection);
            }
        }
    }

    public void rollback() throws DaoException {
        rollbackConnection(connection);
    }
}
