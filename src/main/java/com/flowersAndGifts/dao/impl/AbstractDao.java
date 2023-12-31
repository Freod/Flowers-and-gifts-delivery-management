package com.flowersAndGifts.dao.impl;

import com.flowersAndGifts.database.DatabaseConnection;
import com.flowersAndGifts.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao {
    private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    protected Connection getConnection() throws SQLException {
        return databaseConnection.createConnection();
    }

    protected PreparedStatement getPreparedStatement(final Connection connection, final String query, final List<Object> parameters) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        setPreparedStatementParameters(preparedStatement, parameters);
        return preparedStatement;
    }

    protected void setPreparedStatementParameters(final PreparedStatement preparedStatement, final List<Object> parameters) throws SQLException {
        for (int i = 1; i <= parameters.size(); i++) {
            setPreparedStatementParameter(preparedStatement, i, parameters.get(i - 1));
        }
    }

    protected void setPreparedStatementParameter(PreparedStatement preparedStatement, int index, Object parameter) throws SQLException {
        if (parameter == null) {
            preparedStatement.setString(index, "");
        } else if (Long.class == parameter.getClass()) {
            if((Long) parameter == 0L){
                preparedStatement.setObject(index, null);
            }else {
                preparedStatement.setLong(index, (Long) parameter);
            }
        } else if (Integer.class == parameter.getClass()) {
            preparedStatement.setInt(index, (Integer) parameter);
        } else if (Boolean.class == parameter.getClass()) {
            preparedStatement.setBoolean(index, (Boolean) parameter);
        } else if (String.class == parameter.getClass()) {
            preparedStatement.setString(index, (String) parameter);
        } else if (Double.class == parameter.getClass()) {
            preparedStatement.setDouble(index, (Double) parameter);
        }else if (Role.class == parameter.getClass()){
            preparedStatement.setString(index, parameter.toString());
        }
    }

    protected String setSortAndDirection(final String query, final String sort, final String direction) {
        return String.format(query, sort, direction);
    }
}
