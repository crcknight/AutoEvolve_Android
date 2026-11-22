package com.autoevolve;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.autoevolve.entity.*;
import com.autoevolve.fragment.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> initSeedData()).start();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int id = item.getItemId();
            if (id == R.id.navigation_hero) selected = new HeroFragment();
            else if (id == R.id.navigation_formation) selected = new FormationFragment();
            else if (id == R.id.navigation_battle) selected = new BattleFragment();
            else if (id == R.id.navigation_evolve) selected = new EvolveFragment();
            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selected)
                    .commit();
                return true;
            }
            return false;
        });

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, new HeroFragment())
            .commit();
    }

    private void initSeedData() {
        AppDatabase db = AppDatabase.getInstance(this);
        if (db.heroDao().getAll().isEmpty()) {
            String[][] heroes = {
                {"剑士","1","800","60","1.0","0"},
                {"弓箭手","2","600","80","1.3","1"},
                {"法师","2","500","100","0.8","2"},
                {"骑士","3","1200","50","0.7","0"},
                {"刺客","3","550","110","1.5","1"},
                {"牧师","2","700","40","0.9","2"},
                {"狂战士","4","1000","90","1.1","0"},
                {"狙击手","3","500","130","1.2","1"},
                {"盾卫","1","1500","30","0.6","0"},
                {"术士","4","600","120","0.9","2"},
            };
            for (String[] h : heroes) {
                HeroEntity e = new HeroEntity();
                e.name = h[0]; e.cost = Integer.parseInt(h[1]); e.hp = Integer.parseInt(h[2]);
                e.atk = Integer.parseInt(h[3]); e.atkSpd = Double.parseDouble(h[4]); e.synergyId = Integer.parseInt(h[5]);
                db.heroDao().insert(e);
            }
        }
        if (db.synergyDao().getAll().isEmpty()) {
            String[][] synergies = {
                {"战士之魂","2","1.15","1.0"},
                {"射手专注","2","1.1","1.2"},
                {"法师共鸣","3","1.25","1.0"},
            };
            for (int i = 0; i < synergies.length; i++) {
                SynergyEntity e = new SynergyEntity();
                e.id = i; e.name = synergies[i][0]; e.requiredCount = Integer.parseInt(synergies[i][1]);
                e.atkMultiplier = Double.parseDouble(synergies[i][2]); e.atkSpdMultiplier = Double.parseDouble(synergies[i][3]);
                db.synergyDao().insert(e);
            }
        }
        // 对手阵容不再内置，由用户自行创建
    }
}
