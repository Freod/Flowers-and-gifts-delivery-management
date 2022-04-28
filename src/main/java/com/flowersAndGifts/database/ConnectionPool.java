package com.flowersAndGifts.database;

import com.flowersAndGifts.exception.DaoException;

import java.sql.Connection;

public interface ConnectionPool {
    Connection takeConnection() throws DaoException;

    void retrieveConnection(final Connection connection);
}
