package cn.endereye.service;

import cn.endereye.model.User;
import cn.endereye.util.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class UserService {
    /**
     * Try to create a new user record according to username and password.
     *
     * @param user User object providing username and password.
     * @return User object with ID and other information updated if success, null if failed.
     * @throws SQLException Database error.
     */
    public static User signUp(User user) throws SQLException {
        return Database.execute(connection -> {
            // This inner lambda function returns whether the sign up is successful.
            // Check whether the username is already existed.
            final PreparedStatement stmtQuery =
                    connection.prepareStatement("SELECT * FROM `user` WHERE `username` = ?");
            stmtQuery.setString(1, user.getUsername());
            if (stmtQuery.executeQuery().next())
                return false;
            // Insert new record into database.
            final PreparedStatement stmtUpdate =
                    connection.prepareStatement("INSERT INTO `user` (`username`, `password`) VALUES (?, ?)");
            stmtUpdate.setString(1, user.getUsername());
            stmtUpdate.setString(2, user.getPassword());
            stmtUpdate.executeUpdate();
            return true;
        })
                ? AuthService.signInByUsernameAndPassword(user)
                : null;
    }
}
