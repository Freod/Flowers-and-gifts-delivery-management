package com.flowersAndGifts.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Objects;
import java.util.stream.Collectors;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=public";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private static final DatabaseConnection instance = new DatabaseConnection();

    public static DatabaseConnection getInstance() {
        return instance;
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    private DatabaseConnection() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                try (Statement statement = conn.createStatement()) {
                    statement.execute(getSql("database/init-ddl.sql"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
}
