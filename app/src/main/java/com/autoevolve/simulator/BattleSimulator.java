package com.autoevolve.simulator;

import com.autoevolve.model.*;
import java.util.*;

public class BattleSimulator {
    private static final int MAX_ROUNDS = 100;
    private List<Synergy> synergies;
    private Random random = new Random();

    public BattleSimulator(List<Synergy> synergies) { this.synergies = synergies; }

    /** 对外接口：需要完整日志（UI展示用） */
    public BattleResult simulate(Formation fa, Formation fb) {
        return simulate(fa, fb, true);
    }

    /** 内部接口：可选择是否生成日志（GA演化时传false，大幅提升速度） */
    public BattleResult simulate(Formation fa, Formation fb, boolean generateLog) {
        List<BattleUnit> teamA = buildUnits(fa, 0);
        List<BattleUnit> teamB = buildUnits(fb, 1);
        applySynergies(teamA);
        applySynergies(teamB);

        StringBuilder log = generateLog ? new StringBuilder() : null;
        if (generateLog) {
            log.append("===== 战斗开始 =====\n");
            log.append("【A方阵容】").append(fa.toString()).append("\n");
            log.append("【B方阵容】").append(fb.toString()).append("\n\n");
        }

        int round = 0;
        while (round < MAX_ROUNDS) {
            List<BattleUnit> alive = new ArrayList<>();
            alive.addAll(getAlive(teamA)); alive.addAll(getAlive(teamB));
            if (alive.size() <= 1) break;

            alive.sort((u1, u2) -> Double.compare(u2.getCurAtkSpd(), u1.getCurAtkSpd()));

            if (generateLog) {
                log.append("\n--- 回合 ").append(round + 1).append(" ---\n");
                log.append("行动顺序: ");
                for (BattleUnit u : alive) log.append(u.getHero().getName()).append("(").append(String.format("%.1f", u.getCurAtkSpd())).append(") ");
                log.append("\n");
            }

            for (BattleUnit attacker : alive) {
                if (!attacker.isAlive()) continue;
                List<BattleUnit> enemies = attacker.getTeam() == 0 ? getAlive(teamB) : getAlive(teamA);
                if (enemies.isEmpty()) break;

                BattleUnit target = enemies.get(random.nextInt(enemies.size()));
                int dmg = attacker.getCurAtk();
                target.takeDamage(dmg);

                if (generateLog) {
                    log.append(attacker.getHero().getName()).append(" 攻击 ")
                       .append(target.getHero().getName()).append("，造成 ").append(dmg).append(" 伤害");
                    if (!target.isAlive()) {
                        log.append("，").append(target.getHero().getName()).append(" 阵亡！");
                    } else {
                        log.append("，剩余血量 ").append(target.getCurHp()).append("/").append(target.getHero().getHp());
                    }
                    log.append("\n");
                }
            }

            if (generateLog) {
                int aliveA = getAlive(teamA).size();
                int aliveB = getAlive(teamB).size();
                log.append("回合结束: A方存活 ").append(aliveA).append(" 人，B方存活 ").append(aliveB).append(" 人\n");
            }

            if (getAlive(teamA).isEmpty() || getAlive(teamB).isEmpty()) break;
            round++;
        }

        boolean aWin = !getAlive(teamA).isEmpty();
        int remA = getAlive(teamA).stream().mapToInt(BattleUnit::getCurHp).sum();
        int remB = getAlive(teamB).stream().mapToInt(BattleUnit::getCurHp).sum();

        if (generateLog) {
            log.append("\n===== 战斗结束 =====\n");
            log.append("结果: ").append(aWin ? "A方胜利" : "B方胜利").append("\n");
            log.append("共进行 ").append(round + 1).append(" 回合\n");
            log.append("A方剩余总血量: ").append(remA).append("\n");
            log.append("B方剩余总血量: ").append(remB).append("\n");
        }

        return new BattleResult(aWin ? BattleResult.WIN : BattleResult.LOSE, round + 1, remA, remB,
                generateLog ? log.toString() : "（演化模式，无详细日志）");
    }

    private List<BattleUnit> buildUnits(Formation f, int team) {
        List<BattleUnit> u = new ArrayList<>();
        for (Slot s : f.getSlots()) if (s.getHero() != null) u.add(new BattleUnit(s.getHero(), team));
        return u;
    }

    private List<BattleUnit> getAlive(List<BattleUnit> team) {
        List<BattleUnit> a = new ArrayList<>();
        for (BattleUnit u : team) if (u.isAlive()) a.add(u);
        return a;
    }

    private void applySynergies(List<BattleUnit> team) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (BattleUnit u : team) {
            int sid = u.getHero().getId() % 3;
            cnt.put(sid, cnt.getOrDefault(sid, 0) + 1);
        }
        for (Synergy syn : synergies) {
            if (cnt.getOrDefault(syn.getId(), 0) >= syn.getRequiredCount()) {
                for (BattleUnit u : team) {
                    u.buffAtk(syn.getAtkMultiplier());
                    u.buffSpd(syn.getAtkSpdMultiplier());
                }
            }
        }
    }

    private static class BattleUnit {
        private Hero hero; private int team;
        private int curHp, curAtk; private double curSpd;
        BattleUnit(Hero h, int t) {
            hero = h; team = t; curHp = h.getHp(); curAtk = h.getAtk(); curSpd = h.getAtkSpd();
        }
        void buffAtk(double m) { curAtk = (int)(curAtk * m); }
        void buffSpd(double m) { curSpd = curSpd * m; }
        void takeDamage(int d) { curHp -= d; }
        boolean isAlive() { return curHp > 0; }
        Hero getHero() { return hero; }
        int getTeam() { return team; }
        int getCurHp() { return curHp; }
        int getCurAtk() { return curAtk; }
        double getCurAtkSpd() { return curSpd; }
    }
}
