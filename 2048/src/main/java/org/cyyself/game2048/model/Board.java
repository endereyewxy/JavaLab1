package org.cyyself.game2048.model;

import org.cyyself.game2048.util.Database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Board {
    public static List<UserScore> GetBoard() {
        List<UserScore> res = new ArrayList<>();
        try {
            Database.execute(connection -> {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `score` ORDER BY `score`;");
                while (rs.next()) {
                    res.add(new UserScore(rs.getString("user_id"),rs.getInt("score")));
                }
                return null;
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }
}
