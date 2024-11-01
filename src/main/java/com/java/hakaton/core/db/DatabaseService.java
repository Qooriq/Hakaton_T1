package com.java.hakaton.core.db;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService {

    public int getUserTableSize(String dbIpAddress, int dbPort, String username, String password, String databaseName) {
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", dbIpAddress, dbPort, databaseName);
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS table_size FROM users")) {

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

    public List<List<String>> getUserTableData(String dbIpAddress, int dbPort, String username, String password, String databaseName, int start, int end, String tableName) throws SQLException {
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", dbIpAddress, dbPort, databaseName);
        List<List<String>> data = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            // Retrieve column names
            ResultSet columns = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT 1");
            ResultSetMetaData metaData = columns.getMetaData();
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            data.add(columnNames); // Add column names to the data list

            // Retrieve a single row of data (optional, if you need it)
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT 1");
            if (resultSet.next()) {
                List<String> rowData = new ArrayList<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    rowData.add(resultSet.getString(i));
                }
                data.add(rowData); // Add the row data to the data list
            }

            return data;

        } catch (SQLException e) {
            System.err.println("Error retrieving table data: " + e.getMessage());
            throw e;
        }
    }
}