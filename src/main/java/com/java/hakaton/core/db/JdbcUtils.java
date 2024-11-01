package com.java.hakaton.core.db;

import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class JdbcUtils {

    public int getUserTableSize(String dbIpAddress, int dbPort, String username, String password, String databaseName) {
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", dbIpAddress, dbPort, databaseName);

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS table_size FROM users")) {  // Use users table name

            if (resultSet.next()) {
                return resultSet.getInt("table_size");
            } else {
                System.err.println("Failed to retrieve table size. Result set is empty.");
                return -1;
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving table size: " + e.getMessage());
            return -1;
        }
    }
}