package com.autoevolve.fragment;

import android.app.AlertDialog;
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
import com.autoevolve.agent.Chromosome;
import com.autoevolve.agent.EvolutionAgent;
import com.autoevolve.entity.*;
import com.autoevolve.model.*;
import com.autoevolve.simulator.BattleSimulator;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class EvolveFragment extends Fragment {
    private AppDatabase db;
    private EditText etPop, etGen;
    private Button btnStart, btnAdopt;
    private ProgressBar progress;
    private TextView tvStatus, tvResult;
    private Spinner spinnerOpponent;
    private FitnessCurveView curveView;
    private EvolutionAgent agent;
    private Chromosome lastBest;
    private List<FormationEntity> formations = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evolve, container, false);
        db = AppDatabase.getInstance(requireContext());
        etPop = view.findViewById(R.id.et_pop_size);
        etGen = view.findViewById(R.id.et_generations);
        btnStart = view.findViewById(R.id.btn_start_evolve);
        btnAdopt = view.findViewById(R.id.btn_adopt);
        progress = view.findViewById(R.id.progress_evolve);
        tvStatus = view.findViewById(R.id.tv_evolve_status);
        tvResult = view.findViewById(R.id.tv_recommended_formation);
        spinnerOpponent = view.findViewById(R.id.spinner_opponent);
        curveView = view.findViewById(R.id.fitness_curve);

        loadOpponents();
        btnStart.setOnClickListener(v -> startEvolution());
        btnAdopt.setOnClickListener(v -> showAdoptDialog());
        return view;
    }

    private void loadOpponents() {
        formations = db.formationDao().getAll();
        List<String> names = new ArrayList<>();
        for (FormationEntity e : formations) names.add(e.name + " [费用" + e.totalCost + "]");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpponent.setAdapter(adapter);
    }

    private void startEvolution() {
        if (formations.isEmpty()) {
            Toast.makeText(getContext(), "没有可用阵容", Toast.LENGTH_SHORT).show();
            return;
        }
        btnStart.setEnabled(false);
        progress.setVisibility(View.VISIBLE);
        tvStatus.setText("演化中...");
        tvResult.setText("");
        btnAdopt.setVisibility(View.GONE);
        curveView.setData(new ArrayList<>(), new ArrayList<>());

        int pop = Integer.parseInt(etPop.getText().toString());
        int gen = Integer.parseInt(etGen.getText().toString());

        FormationEntity targetEntity = formations.get(spinnerOpponent.getSelectedItemPosition());
        List<Hero> heroes = loadHeroesFromDb();
        List<Position> pos = new ArrayList<>();
        for (int r = 0; r < 2; r++) for (int c = 0; c < 3; c++) pos.add(new Position(r, c));
        List<Formation> opponents = new ArrayList<>();
        opponents.add(parseFormation(targetEntity));

        List<Synergy> synergies = new ArrayList<>();
        for (SynergyEntity se : db.synergyDao().getAll()) {
            synergies.add(new Synergy(se.id, se.name, se.requiredCount, se.atkMultiplier, se.atkSpdMultiplier));
        }

        agent = new EvolutionAgent(new BattleSimulator(synergies), heroes, pos, opponents, 3);
        agent.setParams(pop, gen, 0.8, 0.15);

        new Thread(() -> {
            try {
                long t0 = System.currentTimeMillis();
                agent.perceive();
                lastBest = agent.decide();
                Formation rec = agent.execute();
                long elapsed = System.currentTimeMillis() - t0;

                new Handler(Looper.getMainLooper()).post(() -> {
                    btnStart.setEnabled(true);
                    progress.setVisibility(View.GONE);
                    tvStatus.setText("演化完成，耗时 " + elapsed + " ms\n最终适应度: " + String.format("%.4f", lastBest.getFitness()));
                    tvResult.setText("推荐阵容: " + rec.toString());
                    btnAdopt.setVisibility(View.VISIBLE);
                    curveView.setData(agent.getBestFitnessHistory(), agent.getAvgFitnessHistory());
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    btnStart.setEnabled(true);
                    progress.setVisibility(View.GONE);
                    tvStatus.setText("演化出错: " + ex.getMessage());
                });
            }
        }).start();
    }

    private void showAdoptDialog() {
        if (lastBest == null) return;
        EditText input = new EditText(requireContext());
        input.setHint("输入阵容名称");
        new AlertDialog.Builder(requireContext())
            .setTitle("采纳推荐阵容")
            .setView(input)
            .setPositiveButton("保存", (dialog, which) -> {
                String name = input.getText().toString().trim();
                if (name.isEmpty()) name = "AI推荐阵容";
                adoptFormation(name);
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void adoptFormation(String name) {
        try {
            List<Position> posList = new ArrayList<>();
            posList.add(new Position(0,0)); posList.add(new Position(0,1)); posList.add(new Position(0,2));
            posList.add(new Position(1,0)); posList.add(new Position(1,1)); posList.add(new Position(1,2));
            Formation rec = lastBest.decode(loadHeroesFromDb(), posList, -1);
            JSONArray arr = new JSONArray();
            for (Slot s : rec.getSlots()) {
                JSONObject obj = new JSONObject();
                obj.put("heroId", s.getHero().getId());
                obj.put("row", s.getPosition().getRow());
                obj.put("col", s.getPosition().getCol());
                arr.put(obj);
            }
            FormationEntity e = new FormationEntity();
            e.name = name; e.slotsJson = arr.toString();
            e.totalCost = rec.getTotalCost(); e.isFavorite = 1;
            db.formationDao().insert(e);
            Toast.makeText(getContext(), "已采纳并保存: " + name, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(getContext(), "保存失败: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<Hero> loadHeroesFromDb() {
        List<Hero> list = new ArrayList<>();
        for (HeroEntity he : db.heroDao().getAll()) {
            list.add(new Hero(he.id, he.name, he.cost, he.hp, he.atk, he.atkSpd));
        }
        return list;
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
