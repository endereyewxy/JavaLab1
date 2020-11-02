// Created by endereyewxy@gmail.com, 2020.10.13

package cn.endereye.service;

import cn.endereye.model.User;
import cn.endereye.util.Database;
import cn.endereye.util.MD5;
import io.jsonwebtoken.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

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

    public static final String KEY_REFRESH =
            Objects.requireNonNull(MD5.md5("KEY_REFRESH" + System.getenv().toString()));

    public static final String KEY_ACCESS =
            Objects.requireNonNull(MD5.md5("KEY_ACCESS" + System.getenv().toString()));

    /**
     * Verify a pair of given username and password.
     *
     * @param user User object providing username and password.
     * @return User object with ID and other information updated if success, null if failed.
     * @throws SQLException Database error.
     */
    public static User signInByUsernameAndPassword(User user) throws SQLException {
        return Database.execute(connection -> {
            final PreparedStatement stmtQuery =
                    connection.prepareStatement("SELECT * FROM `user` WHERE `username` = ? AND `password` = ?");
            stmtQuery.setString(1, user.getUsername());
            stmtQuery.setString(2, user.getPassword());
            // The username and password is correct if and only if the result set is not empty.
            final ResultSet resultSet = stmtQuery.executeQuery();
            if (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                // Update serial number and logout other users.
                final PreparedStatement stmtSignOut =
                        connection.prepareStatement("UPDATE `user` SET `serial` = ? WHERE `id` = ?");
                stmtSignOut.setLong(1, new Date().getTime());
                stmtSignOut.setInt(2, user.getId());
                stmtSignOut.executeUpdate();
                return user;
            }
            return null;
        });
    }

    /**
     * Verify a refresh token.
     *
     * @param token Refresh token string.
     * @return The corresponding user object with everything updated if success, null if failed.
     * @throws SQLException Database error.
     */
    public static User signInByRefreshToken(String token) throws SQLException {
        final Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(KEY_REFRESH.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            // TODO Distinguish between different errors.
            return null;
        }
        return Database.execute(connection -> {
            final PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM `user` WHERE `id` = ?");
            statement.setInt(1, (int) claims.get("id"));
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;
            // Use information from the database instead of the JWT toke, since information may be updated.
            final User user = new User()
                    .setId(resultSet.getInt("id"))
                    .setUsername(resultSet.getString("username"))
                    .setPassword(resultSet.getString("password"))
                    .setSerial(resultSet.getLong("serial"));
            // The issued time must be later than the last sensitive operation time. In other words, there must be no
            // sign out or password change after the token has been issued.
            if (claims.getIssuedAt().before(new Date(user.getSerial())))
                return null;
            return user;
        });
    }

    /**
     * Announce all refresh tokens to be invalid for a certain user.
     *
     * @param user User object providing user ID only.
     * @throws SQLException Database error.
     */
    public static void signOut(User user) throws SQLException {
        Database.execute(connection -> {
            final PreparedStatement statement =
                    connection.prepareStatement("UPDATE `user` SET `serial` = ? WHERE `id` = ?");
            statement.setLong(1, new Date().getTime());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
            return null;
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
