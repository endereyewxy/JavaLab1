package org.cyyself.game2048.model;

import org.cyyself.game2048.util.Database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Board {
    public static List<UserScore> GetBoard() {
        try {
            return Database.execute(connection -> {
                List<UserScore> res = new ArrayList<>();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `user` ORDER BY `score` DESC;");
                while (rs.next()) {
                    res.add(new UserScore(rs.getString("user_id"),rs.getInt("score")));
                }
                return res;
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
