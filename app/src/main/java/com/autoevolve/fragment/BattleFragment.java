package com.autoevolve.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.autoevolve.AppDatabase;
import com.autoevolve.R;
import com.autoevolve.entity.*;
import com.autoevolve.model.*;
import com.autoevolve.simulator.BattleSimulator;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class BattleFragment extends Fragment {
    private AppDatabase db;
    private Spinner spinnerMy, spinnerEnemy;
    private TextView tvLog, tvResult, tvAnimation;
    private List<FormationEntity> allFormations = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battle, container, false);
        db = AppDatabase.getInstance(requireContext());
        spinnerMy = view.findViewById(R.id.spinner_my_formation);
        spinnerEnemy = view.findViewById(R.id.spinner_enemy_formation);
        tvLog = view.findViewById(R.id.tv_battle_log);
        tvResult = view.findViewById(R.id.tv_battle_result);
        tvAnimation = view.findViewById(R.id.tv_battle_animation);

        loadFormations();
        view.findViewById(R.id.btn_start_battle).setOnClickListener(v -> startBattle());
        return view;
    }

    private void loadFormations() {
        allFormations = db.formationDao().getAll();
        List<String> names = new ArrayList<>();
        for (FormationEntity e : allFormations) names.add(e.name + " [费用" + e.totalCost + "]");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMy.setAdapter(adapter);
        spinnerEnemy.setAdapter(adapter);
        if (allFormations.size() > 1) spinnerEnemy.setSelection(1);
    }

    private void startBattle() {
        if (allFormations.isEmpty()) {
            Toast.makeText(getContext(), "没有可用阵容", Toast.LENGTH_SHORT).show();
            return;
        }
        Formation myForm = parseFormation(allFormations.get(spinnerMy.getSelectedItemPosition()));
        Formation enemyForm = parseFormation(allFormations.get(spinnerEnemy.getSelectedItemPosition()));

        List<Synergy> synergies = new ArrayList<>();
        for (SynergyEntity se : db.synergyDao().getAll()) {
            synergies.add(new Synergy(se.id, se.name, se.requiredCount, se.atkMultiplier, se.atkSpdMultiplier));
        }
        BattleSimulator sim = new BattleSimulator(synergies);
        BattleResult result = sim.simulate(myForm, enemyForm);

        // 显示完整战斗日志
        tvAnimation.setText(result.getBattleLog());

        // 战斗结果摘要
        StringBuilder summary = new StringBuilder();
        summary.append("回合数: ").append(result.getRounds()).append("\n");
        summary.append("我方剩余血量: ").append(result.getRemainingHpA()).append("\n");
        summary.append("敌方剩余血量: ").append(result.getRemainingHpB()).append("\n");
        tvLog.setText(summary.toString());

        if (result.isAWin()) {
            tvResult.setText("结果: 胜利!");
            tvResult.setTextColor(getResources().getColor(R.color.win_green));
        } else {
            tvResult.setText("结果: 失败");
            tvResult.setTextColor(getResources().getColor(R.color.lose_red));
        }

        // 保存战绩
        RecordEntity rec = new RecordEntity();
        rec.timestamp = System.currentTimeMillis();
        rec.myFormationId = allFormations.get(spinnerMy.getSelectedItemPosition()).id;
        rec.enemyFormationId = allFormations.get(spinnerEnemy.getSelectedItemPosition()).id;
        rec.result = result.isAWin() ? 1 : 0;
        rec.rounds = result.getRounds();
        db.recordDao().insert(rec);
    }

    private Formation parseFormation(FormationEntity entity) {
        List<Slot> slots = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(entity.slotsJson);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                int heroId = obj.getInt("heroId");
                int row = obj.getInt("row");
                int col = obj.getInt("col");
                HeroEntity he = db.heroDao().getById(heroId);
                if (he != null) {
                    Hero hero = new Hero(he.id, he.name, he.cost, he.hp, he.atk, he.atkSpd);
                    slots.add(new Slot(hero, new Position(row, col)));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return new Formation(entity.id, entity.name, slots);
    }
}
