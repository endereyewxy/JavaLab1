// Created by endereyewxy@gmail.com, 2020.10.13

package cn.endereye.service;

import cn.endereye.model.User;
import cn.endereye.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Login {
    public static boolean verifyPassword(User user) throws SQLException {
        final Connection connection = Database.getConnection();
        final PreparedStatement statement =
                connection.prepareStatement("SELECT * FROM `user` WHERE `username` = ? AND `password` = ?");
        statement.setString(1, user.username);
        statement.setString(2, user.passwordMd5);
        // The username and password is correct if and only if the result set is not empty.
        final boolean result = statement.executeQuery().next();
        Database.closeConnection(connection);
        return result;
    }
}
