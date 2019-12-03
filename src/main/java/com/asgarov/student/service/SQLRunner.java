package com.asgarov.student.service;

import java.io.*;
import java.sql.*;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.asgarov.student.dao.ConnectionFactory;

public class SQLRunner {
    public static void runSQL(String pathToSQLScript) throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionFactory.getConnection()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader(pathToSQLScript));
            scriptRunner.runScript(reader);
        }
    }
}
