package com.autoevolve.model;

import java.util.ArrayList;
import java.util.List;

public class Formation {
    private int id;
    private String name;
    private List<Slot> slots;
    private int totalCost;

    public Formation(int id, String name, List<Slot> slots) {
        this.id = id; this.name = name;
        this.slots = slots != null ? slots : new ArrayList<>();
        recalcCost();
    }
    public void addSlot(Slot slot) { slots.add(slot); recalcCost(); }
    public void recalcCost() {
        totalCost = 0;
        for (Slot s : slots) if (s.getHero() != null) totalCost += s.getHero().getCost();
    }
    public List<Hero> getHeroes() {
        List<Hero> h = new ArrayList<>();
        for (Slot s : slots) if (s.getHero() != null) h.add(s.getHero());
        return h;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public List<Slot> getSlots() { return slots; }
    public int getTotalCost() { return totalCost; }
    public int getHeroCount() { return getHeroes().size(); }
    public String toString() {
        StringBuilder sb = new StringBuilder(name + " [cost=" + totalCost + "]: ");
        for (Slot s : slots) sb.append(s.getHero().getName()).append(s.getPosition()).append(" ");
        return sb.toString();
    }
}
