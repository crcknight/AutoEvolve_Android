package com.autoevolve.utils;

import com.autoevolve.model.*;
import java.util.*;

public class DataProvider {
    public static List<Hero> getHeroes() {
        List<Hero> list = new ArrayList<>();
        list.add(new Hero(0, "剑士", 1, 800, 60, 1.0));
        list.add(new Hero(1, "弓箭手", 2, 600, 80, 1.3));
        list.add(new Hero(2, "法师", 2, 500, 100, 0.8));
        list.add(new Hero(3, "骑士", 3, 1200, 50, 0.7));
        list.add(new Hero(4, "刺客", 3, 550, 110, 1.5));
        list.add(new Hero(5, "牧师", 2, 700, 40, 0.9));
        list.add(new Hero(6, "狂战士", 4, 1000, 90, 1.1));
        list.add(new Hero(7, "狙击手", 3, 500, 130, 1.2));
        list.add(new Hero(8, "盾卫", 1, 1500, 30, 0.6));
        list.add(new Hero(9, "术士", 4, 600, 120, 0.9));
        return list;
    }

    public static List<Synergy> getSynergies() {
        List<Synergy> list = new ArrayList<>();
        list.add(new Synergy(0, "战士之魂", 2, 1.15, 1.0));
        list.add(new Synergy(1, "射手专注", 2, 1.1, 1.2));
        list.add(new Synergy(2, "法师共鸣", 3, 1.25, 1.0));
        return list;
    }

    public static List<Position> getPositions() {
        List<Position> list = new ArrayList<>();
        for (int r = 0; r < 2; r++) for (int c = 0; c < 3; c++) list.add(new Position(r, c));
        return list;
    }

    public static List<Formation> getOpponents(List<Hero> pool, List<Position> pos) {
        List<Formation> ops = new ArrayList<>();
        Random rand = new Random(42);
        for (int f = 0; f < 5; f++) {
            List<Slot> slots = new ArrayList<>();
            Set<Integer> used = new HashSet<>();
            for (int i = 0; i < 3; i++) {
                int hIdx; do { hIdx = rand.nextInt(pool.size()); } while (used.contains(hIdx));
                used.add(hIdx);
                slots.add(new Slot(pool.get(hIdx), pos.get(i)));
            }
            ops.add(new Formation(f, "对手" + (f + 1), slots));
        }
        return ops;
    }
}
