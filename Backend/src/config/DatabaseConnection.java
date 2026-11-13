package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection open(String password) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/todoong";
        String username = "postgres";

        return DriverManager.getConnection(url, username, password);
    }
}