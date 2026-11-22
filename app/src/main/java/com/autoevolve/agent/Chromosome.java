package com.autoevolve.agent;

import com.autoevolve.model.*;
import java.util.ArrayList;
import java.util.List;

public class Chromosome {
    private int[] genes;
    private double fitness = -1;
    public Chromosome(int[] genes) { this.genes = genes.clone(); }
    public int[] getGenes() { return genes; }
    public double getFitness() { return fitness; }
    public void setFitness(double fitness) { this.fitness = fitness; }

    public Formation decode(List<Hero> heroPool, List<Position> positions, int formationId) {
        int k = genes.length / 2;
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int hi = genes[i], pi = genes[k + i];
            if (hi >= 0 && hi < heroPool.size() && pi >= 0 && pi < positions.size()) {
                slots.add(new Slot(heroPool.get(hi), positions.get(pi)));
            }
        }
        return new Formation(formationId, "Evolved", slots);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Chromosome[");
        for (int g : genes) sb.append(g).append(",");
        sb.append("] fit=").append(String.format("%.4f", fitness));
        return sb.toString();
    }
}
