package org.cyyself.game2048.model;

public class DataResult {
    public Integer status;
    public String msg;
    public Integer bestScore;
    public String gameState;
    public DataResult(Integer status,String msg,Integer bestScore,String gameState) {
        this.status = status;
        this.msg = msg;
        this.bestScore = bestScore;
        this.gameState = gameState;
    }
}
