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
    public static final String DB_URL = "";

    public static final int INC_COUNT = 5;
    public static final int MAX_COUNT = 10;

    private static final LinkedList<Connection> CONNECTIONS = new LinkedList<>();

    static {
        try {
            Class.forName(DRIVER);
            increaseConnections();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public synchronized static Connection getConnection() throws SQLException {
        if (CONNECTIONS.isEmpty())
            increaseConnections();
        return CONNECTIONS.removeFirst();
    }

    public synchronized static void closeConnection(Connection connection) throws SQLException {
        connection.close();
        if (CONNECTIONS.size() < MAX_COUNT)
            CONNECTIONS.addLast(connection);
    }

    private static void increaseConnections() throws SQLException {
        for (int i = 0; i < INC_COUNT; i++)
            CONNECTIONS.addLast(DriverManager.getConnection(DB_URL));
    }
}
