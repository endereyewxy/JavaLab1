package org.cyyself.game2048.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
    public static final String DRIVER = System.getProperty("2048.jdbc.driver");
    public static final String DB_URL = System.getProperty("2048.jdbc.db_url");
    @FunctionalInterface
    public interface Task<T> {
        T apply(Connection connection) throws SQLException;
    }
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS `user` (`user_id` TEXT PRIMARY KEY, `score` INTEGER NOT NULL, `status` TEXT);");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static <T> T execute(Task<T> task) throws SQLException {
        final Connection connection = DriverManager.getConnection(DB_URL);
        final T result = task.apply(connection);
        connection.close();
        return result;
    }
}