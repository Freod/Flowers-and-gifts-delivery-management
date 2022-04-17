package com.flowersAndGifts.dao.impl;

import com.flowersAndGifts.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao {
    private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    public Connection connection;

    public AbstractDao() {
        //TODO:change it
        try {
            this.connection = databaseConnection.createConnection();
        } catch (SQLException e) {
            e.printStackTrace();
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
        else if(Double.class == parameter.getClass()){
            preparedStatement.setDouble(index, (Double) parameter);
        }
    }
}
