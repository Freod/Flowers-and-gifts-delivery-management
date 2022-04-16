package com.flowersAndGifts.dao;

import com.flowersAndGifts.database.DatabaseConnection;
import com.flowersAndGifts.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao {
    private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    protected Connection getConnection(final boolean autoCommitStatus) throws DaoException{
        Connection connection;
        try {
            connection = databaseConnection.createConnection();
            connection.setAutoCommit(autoCommitStatus);
            return connection;
        }
        catch (SQLException e){
            throw new DaoException(e);
        }
    }

    protected void closeConnection(Connection connection) throws DaoException {
        try {
            connection.close();
        }
        catch (SQLException e){
            throw new DaoException(e);
        }
    }

    protected void rollbackConnection(Connection connection) throws DaoException{
        try {
            connection.rollback();
            connection.close();
        }
        catch (SQLException e){
            throw new DaoException(e);
        }
    }

    protected PreparedStatement getPreparedStatement(final Connection connection, final String query, final List<Object> parameters) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        setPreparedStatementParameters(preparedStatement, parameters);
        return preparedStatement;
    }

    protected void setPreparedStatementParameters(final PreparedStatement preparedStatement, final List<Object> parameters) throws SQLException {
        for (int i = 1; i <= parameters.size(); i++) {
            setPreparedStatementParameter(preparedStatement, i, parameters.get(i-1));
        }
    }

    protected void setPreparedStatementParameter(PreparedStatement preparedStatement, int index, Object parameter) throws SQLException {
        if(Long.class == parameter.getClass()){
            preparedStatement.setLong(index, (Long) parameter);
        }
        else if(Integer.class == parameter.getClass()){
            preparedStatement.setInt(index, (Integer) parameter);
        }
        else if(String.class == parameter.getClass()){
            preparedStatement.setString(index, (String) parameter);
        }
        else if(Boolean.class == parameter.getClass()){
            preparedStatement.setBoolean(index, (Boolean) parameter);
        }
    }
}
