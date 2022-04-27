package com.flowersAndGifts.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class DatabaseConnection {
    private Properties properties = new Properties();
    private static final String DATABASE_CONFIG_PATH = "database/db.properties";
    private static final String
            DRIVER = "database.driver",
            URL = "database.url",
            DATABASE_NAME = "database.name",
            SCHEMA = "database.schema",
            USERNAME = "database.username",
            PASSWORD = "database.password";
    private boolean driverLoaded = false;

    private static final DatabaseConnection instance = new DatabaseConnection();

    public static DatabaseConnection getInstance() {
        return instance;
    }

    public Connection createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(getProperty(URL) + getProperty(DATABASE_NAME) + "?" + getProperty(SCHEMA), getProperty(USERNAME), getProperty(PASSWORD));
        return connection;
    }

    private DatabaseConnection() {
        loadProperties();
        loadJbdcDriver();
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            try (Connection conn = DriverManager.getConnection(getProperty(URL) + getProperty(DATABASE_NAME) + "?" + getProperty(SCHEMA), getProperty(USERNAME), getProperty(PASSWORD))) {
                try (Statement statement = conn.createStatement()) {
                    statement.execute(getSql("database/init-ddl.sql"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadJbdcDriver() {
        if (!driverLoaded) {
            try {
                Class.forName(getProperty(DRIVER));
                driverLoaded = true;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String getSql(final String resourceName) {
        return new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(
                                DatabaseConnection.class.getClassLoader().getResourceAsStream(resourceName))))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private void loadProperties() {
        try (InputStream inputStream = DatabaseConnection.class.getClassLoader().getResourceAsStream(DATABASE_CONFIG_PATH)) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getProperty(String key) {
        return properties.getProperty(key);
    }
}
