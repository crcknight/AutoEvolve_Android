package com.autoevolve.model;

public class Hero {
    private int id;
    private String name;
    private int cost;
    private int hp;
    private int atk;
    private double atkSpd;

    public Hero(int id, String name, int cost, int hp, int atk, double atkSpd) {
        this.id = id; this.name = name; this.cost = cost;
        this.hp = hp; this.atk = atk; this.atkSpd = atkSpd;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public int getCost() { return cost; }
    public int getHp() { return hp; }
    public int getAtk() { return atk; }
    public double getAtkSpd() { return atkSpd; }
    public String toString() {
        return String.format("%s(cost=%d, hp=%d, atk=%d, spd=%.1f)", name, cost, hp, atk, atkSpd);
    }
}
