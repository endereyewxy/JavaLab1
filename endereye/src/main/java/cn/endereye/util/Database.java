// Created by endereyewxy@gmail.com, 2020.10.12

package cn.endereye.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * JDBC connection pool implementation.
 */
public abstract class Database {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:/home/endereye/Workspace/JavaLab1/endereye/test.sqlite";

    public static final int INC_COUNT = 5;
    public static final int MAX_COUNT = 10;

    @FunctionalInterface
    public interface Task<T> {
        T apply(Connection connection) throws SQLException;
    }

    private static final LinkedList<Connection> CONNECTIONS = new LinkedList<>();

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public synchronized static Connection getConnection() throws SQLException {
        if (CONNECTIONS.isEmpty()) {
            increaseConnections();
        }
        return CONNECTIONS.removeFirst();
    }

    public synchronized static void closeConnection(Connection connection) throws SQLException {
        connection.close();
        if (CONNECTIONS.size() < MAX_COUNT)
            CONNECTIONS.addLast(connection);
    }

    public static <T> T execute(Task<T> task) throws SQLException {
        final Connection connection = getConnection();
        final T result = task.apply(connection);
        closeConnection(connection);
        return result;
    }

    private static void increaseConnections() throws SQLException {
        for (int i = 0; i < INC_COUNT; i++)
            CONNECTIONS.addLast(DriverManager.getConnection(DB_URL));
    }
}
