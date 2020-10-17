// Created by endereyewxy@gmail.com, 2020.10.13

package cn.endereye.service;

import cn.endereye.model.User;
import cn.endereye.util.Database;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Login {
    public static final long TTL = 86400000; // A day

    public static final String KEY = "123456"; // TODO

    /**
     * Verity the correctness of username and password.
     *
     * @param user Object to be verified.
     * @return JWT token if verification succeeded, otherwise null.
     * @throws SQLException Database error.
     */
    public static String verifyPassword(User user) throws SQLException {
        final Connection connection = Database.getConnection();
        final PreparedStatement statement =
                connection.prepareStatement("SELECT * FROM `user` WHERE `username` = ? AND `password` = ?");
        statement.setString(1, user.username);
        statement.setString(2, user.passwordMd5);
        // The username and password is correct if and only if the result set is not empty.
        final String result = statement.executeQuery().next()
                ? generateJWTToken(user)
                : null;
        Database.closeConnection(connection);
        return result;
    }

    private static String generateJWTToken(User user) {
        final long currTime = System.currentTimeMillis();
        return Jwts.builder()
                .setId("id") // TODO ???
                .setIssuedAt(new Date(currTime))
                .setExpiration(new Date(currTime + TTL))
                .setSubject(user.username)
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }
}
