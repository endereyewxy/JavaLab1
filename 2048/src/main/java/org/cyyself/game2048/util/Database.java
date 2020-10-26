package org.cyyself.game2048.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:/Users/cyy/JavaLab1/2048/db/main.db";
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
            statement.execute("CREATE TABLE IF NOT EXISTS `score` (`user_id` TEXT PRIMARY KEY, `score` INTEGER);");
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