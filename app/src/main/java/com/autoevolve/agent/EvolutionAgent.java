package com.autoevolve.agent;

import com.autoevolve.model.*;
import com.autoevolve.simulator.BattleSimulator;
import java.util.List;

public class EvolutionAgent {
    private BattleSimulator simulator;
    private List<Hero> heroPool;
    private List<Position> positions;
    private List<Formation> opponents;
    private int teamSize;
    private int popSize = 80, generations = 50;
    private double crossoverRate = 0.8, mutationRate = 0.15;
    private Chromosome lastBest;
    private List<Double> lastBestHistory, lastAvgHistory;

    public EvolutionAgent(BattleSimulator sim, List<Hero> pool, List<Position> pos, List<Formation> ops, int teamSize) {
        this.simulator = sim; this.heroPool = pool; this.positions = pos;
        this.opponents = ops; this.teamSize = teamSize;
    }

    public void setParams(int popSize, int generations, double cxRate, double mutRate) {
        this.popSize = popSize; this.generations = generations;
        this.crossoverRate = cxRate; this.mutationRate = mutRate;
    }

    public void perceive() {}

    public Chromosome decide() {
        FitnessEvaluator ev = new FitnessEvaluator(simulator, heroPool, positions, opponents, 10);
        GeneticAlgorithm ga = new GeneticAlgorithm(popSize, generations, crossoverRate, mutationRate,
                heroPool.size(), positions.size(), teamSize, ev);
        lastBest = ga.evolve();
        lastBestHistory = ga.getBestFitnessHistory();
        lastAvgHistory = ga.getAvgFitnessHistory();
        return lastBest;
    }

    public Formation execute() {
        return lastBest == null ? null : lastBest.decode(heroPool, positions, -1);
    }

    public void feedback(Formation myForm, Formation enemyForm, boolean win) {
        // 实际应写入数据库；此处留接口
    }

    public List<Double> getBestFitnessHistory() { return lastBestHistory; }
    public List<Double> getAvgFitnessHistory() { return lastAvgHistory; }
}
