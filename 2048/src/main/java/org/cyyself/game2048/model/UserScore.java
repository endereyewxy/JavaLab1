package org.cyyself.game2048.model;

public class UserScore {
    public String user_id;
    public Integer score;
    public UserScore(String user_id,Integer score) {
        this.user_id = user_id;
        this.score = score;
    }
}
