package cn.endereye.service;

import cn.endereye.model.User;
import cn.endereye.util.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class UserService {
    public static User signUp(User user) throws SQLException {
        return Database.execute(connection -> {
            final PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO `user` (`username`, `password`) VALUES (?,?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
            return AuthService.signInByUsernameAndPassword(user);
        });
    }

}
