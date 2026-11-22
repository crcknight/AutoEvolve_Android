package com.autoevolve.model;

public class BattleResult {
    public static final int WIN = 1, LOSE = 0;
    private int winner, rounds, remainingHpA, remainingHpB;
    private String battleLog;

    public BattleResult(int winner, int rounds, int remHpA, int remHpB, String log) {
        this.winner = winner; this.rounds = rounds;
        this.remainingHpA = remHpA; this.remainingHpB = remHpB;
        this.battleLog = log;
    }

    public boolean isAWin() { return winner == WIN; }
    public int getWinner() { return winner; }
    public int getRounds() { return rounds; }
    public int getRemainingHpA() { return remainingHpA; }
    public int getRemainingHpB() { return remainingHpB; }
    public String getBattleLog() { return battleLog; }
}
