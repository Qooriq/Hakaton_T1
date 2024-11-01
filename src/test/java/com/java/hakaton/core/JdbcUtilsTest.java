/*
package com.java.hakaton.core;

import com.java.hakaton.core.db.JdbcUtils;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JdbcUtilsTest {

*/
/*
    @Test
    void generateConnectionKey_shouldReturnCorrectKey() {
        DatabaseConnector connector = new DatabaseConnector();
        String key = connector.generateConnectionKey("127.0.0.1", "5432", "user");
        assertEquals("127.0.0.1:5432:user", key);
    }
*//*


    String ipAddress = "127.0.0.1";
    String port = "5432";
    String userName = "wayzap";
    String password = "password";
    String databaseName = "hakaton";


    @Test
    void createConnection_successfulConnection() throws SQLException, ClassNotFoundException {
        JdbcUtils databaseConnector = new JdbcUtils();
        JdbcConnection connection = databaseConnector.createConnection(ipAddress, port, userName, password, databaseName);
        assertNotNull(connection);
    }

    @Test
    void testGettinData(){
        JdbcUtils databaseConnector = new JdbcUtils();
    }

*/
/*

    @Test
    void createConnection_SQLException() throws ClassNotFoundException {

        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenThrow(new SQLException("Simulated SQL Exception"));

            DatabaseConnector connector = new DatabaseConnector();
            DatabaseConnector.JdbcConnection jdbcConnection = connector.createConnection("127.0.0.1", "5432", "user", "password", "database");

            assertNull(jdbcConnection); // Expect null due to the exception
        }
    }

    @Test
    void createConnection_ClassNotFoundException() throws SQLException {
        try (MockedStatic<Class> mockedClass = mockStatic(Class.class)) {
            mockedClass.when(()-> Class.forName("org.postgresql.Driver")).thenThrow(new ClassNotFoundException("Simulated ClassNotFoundException"));
            DatabaseConnector connector = new DatabaseConnector();
            DatabaseConnector.JdbcConnection jdbcConnection = connector.createConnection("127.0.0.1", "5432", "user", "password", "database");
            assertNull(jdbcConnection);

        }


    }

    @Test
    void jdbcConnectionClose_shouldCloseConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        DatabaseConnector.JdbcConnection jdbcConnection = new DatabaseConnector.JdbcConnection(mockConnection);

        jdbcConnection.close();

        verify(mockConnection, times(1)).close();
    }


    @Test
    void jdbcConnectionClose_handlesNullConnection() throws SQLException {
        DatabaseConnector.JdbcConnection jdbcConnection = new DatabaseConnector.JdbcConnection(null);
        assertDoesNotThrow(jdbcConnection::close);
    }

    @Test
    void jdbcConnectionClose_handlesClosedConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isClosed()).thenReturn(true); // Simulate already closed connection
        DatabaseConnector.JdbcConnection jdbcConnection = new DatabaseConnector.JdbcConnection(mockConnection);

        assertDoesNotThrow(jdbcConnection::close);
        verify(mockConnection, times(0)).close(); // Verify close() isn't called again.


    }*//*


}*/
