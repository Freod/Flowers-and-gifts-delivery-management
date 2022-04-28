package com.flowersAndGifts.database;

import com.flowersAndGifts.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionPoolImpl implements ConnectionPool {
    private static final int CONNECTIONS_TOTAL = 4;
    private final ArrayBlockingQueue<Connection> pool = new ArrayBlockingQueue<>(CONNECTIONS_TOTAL);
    private final ArrayBlockingQueue<Connection> taken = new ArrayBlockingQueue<>(CONNECTIONS_TOTAL);
    private DatabaseConfig databaseConfig = new DatabaseConfig();
    private final static ConnectionPoolImpl instance = new ConnectionPoolImpl();

    private ConnectionPoolImpl() {
        initConnectionPool();
    }

    private void initConnectionPool() {
        try {
            for (int i = 0; i < CONNECTIONS_TOTAL; i++) {
                pool.add(databaseConfig.createConnection());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionPoolImpl getInstance(){
        return instance;
    }

    @Override
    public Connection takeConnection() throws DaoException {
        Connection connection;
        try {
            connection = pool.take();
            taken.add(connection);
        } catch (InterruptedException e) {
            throw new DaoException(e);
        }
        return connection;
    }

    @Override
    public void retrieveConnection(final Connection connection) {
        if (connection != null) {
            taken.remove(connection);
            pool.add(connection);
        }
    }
}
