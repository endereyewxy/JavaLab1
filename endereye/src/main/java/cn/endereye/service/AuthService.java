// Created by endereyewxy@gmail.com, 2020.10.13

package cn.endereye.service;

import cn.endereye.model.User;
import cn.endereye.util.Database;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Provide services to authenticate a user or token. There are two types of tokens: refresh token and access token. Both
 * tokens are stateless JWTs and are stored in cookies.
 * The refresh token has a long TTL, but can only be accessed by the authentication server. This type of token acted as
 * a replacement of username and password when signing in.
 * The access token has a relatively short TTL. It can be accessed by the resource server, and provides non-sensitive
 * information.
 */
public abstract class AuthService {
    public static final long TTL_REFRESH = 86400000L; // a day

    public static final long TTL_ACCESS = 600000L; // ten minutes

    public static final String KEY_REFRESH = "some key";

    public static final String KEY_ACCESS = "another key";

    public static User signInByUsernameAndPassword(User user) throws SQLException {
        return Database.execute(connection -> {
            final PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM `user` WHERE `username` = ? AND `password` = ?");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            // The username and password is correct if and only if the result set is not empty.
            final ResultSet resultSet = statement.executeQuery();
            return resultSet.next()
                    ? user.setId(resultSet.getInt("id"))
                    : null;
        });
    }

    public static User signInByRefreshToken(String token) throws SQLException {
        final Claims claims = Jwts.parser()
                .setSigningKey(KEY_REFRESH.getBytes())
                .parseClaimsJws(token)
                .getBody();
        // Validate expiration time. Simply returns failure if expiration time is not before the current time.
        if (claims.getExpiration().before(new Date()))
            return null;
        return Database.execute(connection -> {
            final PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM `user` WHERE `id` = ?");
            statement.setInt(1, (int) claims.get("id"));
            final ResultSet resultSet = statement.executeQuery();
            // Get information from the database instead of the JWT toke, since information may be updated.
            return resultSet.next()
                    ? (
                    new User()
                            .setId(resultSet.getInt("id"))
                            .setUsername(resultSet.getString("username"))
                            .setPassword(resultSet.getString("password")))
                    : null;
        });
    }

    public static String acquireRefreshToken(User user) {
        return buildJWT(user, TTL_REFRESH, KEY_REFRESH);
    }

    public static String acquireAccessToken(User user) {
        return buildJWT(user, TTL_ACCESS, KEY_ACCESS);

    }

    private static String buildJWT(User user, long ttl, String key) {
        final long currTime = System.currentTimeMillis();
        return Jwts.builder()
                .claim("id", user.getId())
                .claim("username", user.getUsername())
                .setIssuedAt(new Date(currTime))
                .setExpiration(new Date(currTime + ttl))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();
    }
}
