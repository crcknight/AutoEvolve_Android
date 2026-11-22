package com.autoevolve.agent;

import java.util.*;

public class GeneticAlgorithm {
    private int popSize, generations, chromosomeLen, heroPoolSize, positionCount;
    private double crossoverRate, mutationRate;
    private FitnessEvaluator evaluator;
    private Random random;
    private List<Double> bestHistory = new ArrayList<>();
    private List<Double> avgHistory = new ArrayList<>();

    public GeneticAlgorithm(int popSize, int gens, double cxRate, double mutRate,
                            int heroPoolSize, int posCount, int teamSize, FitnessEvaluator ev) {
        this.popSize = popSize; this.generations = gens;
        this.crossoverRate = cxRate; this.mutationRate = mutRate;
        this.heroPoolSize = heroPoolSize; this.positionCount = posCount;
        this.chromosomeLen = teamSize * 2;
        this.evaluator = ev;
        this.random = new Random();
    }

    public Chromosome evolve() {
        List<Chromosome> pop = initPopulation();
        evaluate(pop);
        for (int g = 0; g < generations; g++) {
            List<Chromosome> next = new ArrayList<>();
            Chromosome best = getBest(pop);
            next.add(new Chromosome(best.getGenes()));
            next.get(0).setFitness(best.getFitness());
            while (next.size() < popSize) {
                Chromosome p1 = tournamentSelect(pop);
                Chromosome p2 = tournamentSelect(pop);
                Chromosome[] kids = crossover(p1, p2);
                mutate(kids[0]); mutate(kids[1]);
                next.add(kids[0]);
                if (next.size() < popSize) next.add(kids[1]);
            }
            evaluate(next);
            pop = next;
            bestHistory.add(getBest(pop).getFitness());
            avgHistory.add(pop.stream().mapToDouble(Chromosome::getFitness).average().orElse(0));
        }
        return getBest(pop);
    }

    public List<Double> getBestFitnessHistory() { return bestHistory; }
    public List<Double> getAvgFitnessHistory() { return avgHistory; }

    private List<Chromosome> initPopulation() {
        List<Chromosome> pop = new ArrayList<>();
        int k = chromosomeLen / 2;
        for (int i = 0; i < popSize; i++) {
            int[] g = new int[chromosomeLen];
            for (int j = 0; j < k; j++) g[j] = random.nextInt(heroPoolSize);
            List<Integer> pl = new ArrayList<>();
            for (int p = 0; p < positionCount; p++) pl.add(p);
            Collections.shuffle(pl);
            for (int j = 0; j < k; j++) g[k + j] = pl.get(j);
            pop.add(new Chromosome(g));
        }
        return pop;
    }

    private void evaluate(List<Chromosome> pop) {
        for (Chromosome c : pop) if (c.getFitness() < 0) c.setFitness(evaluator.evaluate(c));
    }

    private Chromosome tournamentSelect(List<Chromosome> pop) {
        Chromosome best = null;
        for (int i = 0; i < 3; i++) {
            Chromosome cand = pop.get(random.nextInt(pop.size()));
            if (best == null || cand.getFitness() > best.getFitness()) best = cand;
        }
        return new Chromosome(best.getGenes());
    }

    private Chromosome[] crossover(Chromosome p1, Chromosome p2) {
        if (random.nextDouble() > crossoverRate) {
            return new Chromosome[]{new Chromosome(p1.getGenes()), new Chromosome(p2.getGenes())};
        }
        int point = random.nextInt(chromosomeLen - 1) + 1;
        int[] g1 = new int[chromosomeLen], g2 = new int[chromosomeLen];
        for (int i = 0; i < chromosomeLen; i++) {
            if (i < point) { g1[i] = p1.getGenes()[i]; g2[i] = p2.getGenes()[i]; }
            else { g1[i] = p2.getGenes()[i]; g2[i] = p1.getGenes()[i]; }
        }
        return new Chromosome[]{new Chromosome(g1), new Chromosome(g2)};
    }

    private void mutate(Chromosome c) {
        int[] g = c.getGenes(); int k = chromosomeLen / 2;
        for (int i = 0; i < chromosomeLen; i++) {
            if (random.nextDouble() < mutationRate) {
                g[i] = (i < k) ? random.nextInt(heroPoolSize) : random.nextInt(positionCount);
            }
        }
    }

    private Chromosome getBest(List<Chromosome> pop) {
        Chromosome b = pop.get(0);
        for (Chromosome c : pop) if (c.getFitness() > b.getFitness()) b = c;
        return b;
    }
}
