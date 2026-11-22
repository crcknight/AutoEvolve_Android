package com.autoevolve.model;

public class Synergy {
    private int id;
    private String name;
    private int requiredCount;
    private double atkMultiplier;
    private double atkSpdMultiplier;

    public Synergy(int id, String name, int requiredCount, double atkMul, double spdMul) {
        this.id = id; this.name = name; this.requiredCount = requiredCount;
        this.atkMultiplier = atkMul; this.atkSpdMultiplier = spdMul;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public int getRequiredCount() { return requiredCount; }
    public double getAtkMultiplier() { return atkMultiplier; }
    public double getAtkSpdMultiplier() { return atkSpdMultiplier; }
}
