package org.cyyself.game2048.model;

import org.cyyself.game2048.util.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class UserScore {
    public String user_id;
    private Integer score = null;
    public UserScore(String user_id) {
        this.user_id = user_id;
        try {
            Database.execute(connection -> {
                PreparedStatement stmt = connection.prepareStatement("INSERT OR IGNORE INTO `score` VALUES(?,0);");
                stmt.setString(1,user_id);
                stmt.execute();
                return null;
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public UserScore(String user_id,Integer score) {
        this.user_id = user_id;
        this.score = score;
    }
    public Integer GetScore() {
        if (score != null) return score;
        else {
            AtomicReference<Integer> result = new AtomicReference<>(0);
            try {
                Database.execute(connection -> {
                    PreparedStatement stmt = connection.prepareStatement("SELECT `score` FROM `score` WHERE `user_id` = ? LIMIT 1;");
                    stmt.setString(1,user_id);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        result.set(rs.getInt("score"));
                    }
                    return null;
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return result.get();
        }
    }
    public boolean SetScore(int new_score) {
        try {
            Database.execute(connection -> {
                PreparedStatement stmt = connection.prepareStatement("UPDATE `score` SET `score` = ? WHERE `user_id` = ?;");
                stmt.setInt(1,new_score);
                stmt.setString(2,user_id);
                stmt.execute();
                return null;
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
}
