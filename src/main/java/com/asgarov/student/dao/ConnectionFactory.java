package com.asgarov.student.dao;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class ConnectionFactory {
    private static String CONNECTION_URL;

    private static String USER;
    private static String PASS;

    static {
        try (InputStream input = new FileInputStream("src//resources/db.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            CONNECTION_URL = properties.getProperty("CONNECTION_URL");
            USER = properties.getProperty("USER");
            PASS = properties.getProperty("PASS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, USER, PASS);
    }
}
