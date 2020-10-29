package org.cyyself.game2048.model;

import org.cyyself.game2048.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String user_id;
    public User(String user_id) {
        this.user_id = user_id;
        try {
            Database.execute(connection -> {
                PreparedStatement stmt = connection.prepareStatement("INSERT OR IGNORE INTO `user` VALUES(?,0,'{\"grid\":{\"size\":4,\"cells\":[[null,null,null,null],[null,null,null,null],[null,null,null,null],[{\"position\":{\"x\":3,\"y\":0},\"value\":2},null,{\"position\":{\"x\":3,\"y\":2},\"value\":2},null]]},\"score\":0,\"over\":false,\"won\":false,\"keepPlaying\":false}');");
                stmt.setString(1,user_id);
                stmt.execute();
                return null;
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public Integer getScore() {
        try {
            return Database.execute(connection -> {
                PreparedStatement stmt = connection.prepareStatement("SELECT `score` FROM `user` WHERE `user_id` = ? LIMIT 1;");
                stmt.setString(1,user_id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getInt("score") : null;
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
    public boolean setScore(Integer new_score) {
        try {
            return Database.execute(connection -> {
                PreparedStatement stmt = connection.prepareStatement("UPDATE `user` SET `score` = ? WHERE `user_id` = ? AND `score` < ?;");
                stmt.setInt(1,new_score);
                stmt.setString(2,user_id);
                stmt.setInt(3,new_score);
                stmt.execute();
                return true;
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
    public String getStatus() {
        try {
            return Database.execute(connection -> {
                PreparedStatement stmt = connection.prepareStatement("SELECT `status` FROM `user` WHERE `user_id` = ? LIMIT 1;");
                stmt.setString(1,user_id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getString("status") : null;
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
    public boolean setStatus(String new_status) {
        try {
            return Database.execute(connection -> {
                PreparedStatement stmt = connection.prepareStatement("UPDATE `user` SET `status` = ? WHERE `user_id` = ?;");
                stmt.setString(1,new_status);
                stmt.setString(2,user_id);
                stmt.execute();
                return true;
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
