// Created by endereyewxy@gmail.com, 2020.10.12

package cn.endereye.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Database {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:/home/endereye/Workspace/JavaLab1/endereye/test.sqlite";

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
    }

    public static <T> T execute(Task<T> task) throws SQLException {
        final Connection connection = DriverManager.getConnection(DB_URL);
        final T result = task.apply(connection);
        connection.close();
        return result;
    }
}
