package com.autoevolve.agent;

import com.autoevolve.model.*;
import com.autoevolve.simulator.BattleSimulator;
import java.util.List;
import java.util.Random;

public class FitnessEvaluator {
    private BattleSimulator simulator;
    private List<Hero> heroPool;
    private List<Position> positions;
    private List<Formation> opponents;
    private int battleCount;

    public FitnessEvaluator(BattleSimulator sim, List<Hero> pool, List<Position> pos, List<Formation> ops, int bc) {
        this.simulator = sim; this.heroPool = pool; this.positions = pos;
        this.opponents = ops; this.battleCount = bc;
    }

    public double evaluate(Chromosome chromosome) {
        Formation mine = chromosome.decode(heroPool, positions, -1);
        if (mine.getHeroCount() == 0) return 0.0;
        // 检测重复英雄
        java.util.Set<Integer> heroIds = new java.util.HashSet<>();
        for (Hero h : mine.getHeroes()) {
            if (!heroIds.add(h.getId())) return 0.0; // 有重复，fitness = 0
        }
        int wins = 0;
        Random r = new Random();
        for (int i = 0; i < battleCount; i++) {
            Formation enemy = opponents.get(r.nextInt(opponents.size()));
            if (simulator.simulate(mine, enemy, false).isAWin()) wins++;
        }
        return (double) wins / battleCount;
    }
}
