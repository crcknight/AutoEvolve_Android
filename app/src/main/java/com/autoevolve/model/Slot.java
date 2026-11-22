package com.autoevolve.model;

public class Slot {
    private Hero hero;
    private Position position;
    public Slot(Hero hero, Position position) { this.hero = hero; this.position = position; }
    public Hero getHero() { return hero; }
    public Position getPosition() { return position; }
    public void setHero(Hero hero) { this.hero = hero; }
    public void setPosition(Position position) { this.position = position; }
}
