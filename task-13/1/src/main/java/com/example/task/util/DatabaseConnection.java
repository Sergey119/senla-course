package com.example.task.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String propertiesFile = "/application.properties";
    private static final String urlProperty = "spring.datasource.url";
    private static final String usernameProperty = "spring.datasource.username";
    private static final String passwordProperty = "spring.datasource.password";
    private static final String driverProperty = "spring.datasource.driver-class-name";

    private static DatabaseConnection instance;
    private Connection connection;
    private final Properties properties;

    private DatabaseConnection() {
        properties = loadProperties();
        loadDriver();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        var url = properties.getProperty(urlProperty);
        var username = properties.getProperty(usernameProperty);
        var password = properties.getProperty(passwordProperty);

        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(true);

        return connection;
    }

    public Connection getConnectionWithoutAutoCommit() throws SQLException {
        var conn = getConnection();
        conn.setAutoCommit(false);
        return conn;
    }

    private Properties loadProperties() {
        var props = new Properties();
        try (var input = getClass().getResourceAsStream(propertiesFile)) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
        return props;
    }

    private void loadDriver() {
        try {
            Class.forName(properties.getProperty(driverProperty));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database driver", e);
        }
    }
}