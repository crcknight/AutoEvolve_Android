package com.autoevolve.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.autoevolve.AppDatabase;
import com.autoevolve.R;
import com.autoevolve.entity.FormationEntity;
import com.autoevolve.entity.HeroEntity;
import com.autoevolve.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FormationFragment extends Fragment {
    private List<HeroEntity> heroes;
    private List<Position> positions;
    private List<Slot> currentSlots = new ArrayList<>();
    private GridLayout gridPositions;
    private TextView tvCost, tvSavedList;
    private Spinner spinnerHeroes;
    private HeroEntity selectedHero;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_formation, container, false);
        db = AppDatabase.getInstance(requireContext());
        heroes = db.heroDao().getAll();
        positions = new ArrayList<>();
        for (int r = 0; r < 2; r++) for (int c = 0; c < 3; c++) positions.add(new Position(r, c));

        gridPositions = view.findViewById(R.id.grid_positions);
        tvCost = view.findViewById(R.id.tv_formation_cost);
        tvSavedList = view.findViewById(R.id.tv_saved_formations);
        spinnerHeroes = view.findViewById(R.id.spinner_heroes);

        // Spinner
        List<String> heroNames = new ArrayList<>();
        for (HeroEntity h : heroes) heroNames.add(h.name + " (费用" + h.cost + ")");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, heroNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeroes.setAdapter(adapter);
        spinnerHeroes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { selectedHero = heroes.get(pos); }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        if (!heroes.isEmpty()) selectedHero = heroes.get(0);

        // 2x3 Grid
        for (int i = 0; i < 6; i++) {
            Button btn = new Button(requireContext());
            btn.setText("(" + (i/3) + "," + (i%3) + ")");
            btn.setTextSize(12);
            final int idx = i;
            btn.setOnClickListener(v -> placeHero(idx, btn));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(i % 3, 1f);
            params.rowSpec = GridLayout.spec(i / 3);
            params.setMargins(4, 4, 4, 4);
            gridPositions.addView(btn, params);
            currentSlots.add(new Slot(null, positions.get(i)));
        }

        view.findViewById(R.id.btn_save_formation).setOnClickListener(v -> showSaveDialog());
        view.findViewById(R.id.btn_clear_formation).setOnClickListener(v -> clearFormation());
        view.findViewById(R.id.btn_manage_formations).setOnClickListener(v -> showManageDialog());

        refreshSavedList();
        return view;
    }

    private void placeHero(int posIdx, Button btn) {
        if (selectedHero == null) return;
        currentSlots.get(posIdx).setHero(new Hero(selectedHero.id, selectedHero.name, selectedHero.cost, selectedHero.hp, selectedHero.atk, selectedHero.atkSpd));
        btn.setText(selectedHero.name + "\n" + positions.get(posIdx));
        updateCost();
    }

    private void clearFormation() {
        for (int i = 0; i < 6; i++) {
            currentSlots.get(i).setHero(null);
            ((Button)gridPositions.getChildAt(i)).setText("(" + (i/3) + "," + (i%3) + ")");
        }
        updateCost();
    }

    private void updateCost() {
        int cost = 0;
        for (Slot s : currentSlots) if (s.getHero() != null) cost += s.getHero().getCost();
        tvCost.setText("费用: " + cost);
    }

    private void showSaveDialog() {
        EditText input = new EditText(requireContext());
        input.setHint("输入阵容名称");
        new AlertDialog.Builder(requireContext())
            .setTitle("保存阵容")
            .setView(input)
            .setPositiveButton("保存", (dialog, which) -> {
                String name = input.getText().toString().trim();
                if (name.isEmpty()) name = "未命名阵容";
                saveFormation(name);
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private static final int MAX_COST = 15;

    private void saveFormation(String name) {
        if (name == null || name.trim().isEmpty()) {
            Toast.makeText(getContext(), "阵容名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        int cost = 0;
        java.util.Set<Integer> heroIds = new java.util.HashSet<>();
        for (Slot s : currentSlots) {
            if (s.getHero() != null) {
                cost += s.getHero().getCost();
                if (!heroIds.add(s.getHero().getId())) {
                    Toast.makeText(getContext(), "阵容中有重复英雄！每个英雄最多出现一次", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if (cost > MAX_COST) {
            Toast.makeText(getContext(), "费用超限！当前" + cost + "，上限" + MAX_COST, Toast.LENGTH_SHORT).show();
            return;
        }
        if (cost == 0) {
            Toast.makeText(getContext(), "棋盘为空，无法保存", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONArray arr = new JSONArray();
            for (int i = 0; i < 6; i++) {
                Slot s = currentSlots.get(i);
                if (s.getHero() != null) {
                    JSONObject obj = new JSONObject();
                    obj.put("heroId", s.getHero().getId());
                    obj.put("row", s.getPosition().getRow());
                    obj.put("col", s.getPosition().getCol());
                    arr.put(obj);
                }
            }
            FormationEntity e = new FormationEntity();
            e.name = name;
            e.slotsJson = arr.toString();
            e.totalCost = 0;
            for (Slot s : currentSlots) if (s.getHero() != null) e.totalCost += s.getHero().getCost();
            e.isFavorite = 1;
            db.formationDao().insert(e);
            Toast.makeText(getContext(), "已保存: " + name, Toast.LENGTH_SHORT).show();
            refreshSavedList();
        } catch (Exception ex) {
            Toast.makeText(getContext(), "保存失败: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshSavedList() {
        List<FormationEntity> list = db.formationDao().getAll();
        StringBuilder sb = new StringBuilder("已保存阵容 (" + list.size() + "):\n");
        for (FormationEntity e : list) {
            sb.append("• ").append(e.name).append(" [费用").append(e.totalCost).append("]\n");
        }
        tvSavedList.setText(sb.toString());
    }

    private void showManageDialog() {
        List<FormationEntity> list = db.formationDao().getAll();
        if (list.isEmpty()) {
            Toast.makeText(getContext(), "没有已保存的阵容", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) items[i] = list.get(i).name;
        new AlertDialog.Builder(requireContext())
            .setTitle("管理阵容")
            .setItems(items, (dialog, which) -> showItemActions(list.get(which)))
            .setNegativeButton("取消", null)
            .show();
    }

    private void showItemActions(FormationEntity entity) {
        new AlertDialog.Builder(requireContext())
            .setTitle(entity.name)
            .setItems(new String[]{"改名", "删除"}, (dialog, which) -> {
                if (which == 0) showRenameDialog(entity);
                else deleteFormation(entity);
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void showRenameDialog(FormationEntity entity) {
        EditText input = new EditText(requireContext());
        input.setText(entity.name);
        new AlertDialog.Builder(requireContext())
            .setTitle("改名")
            .setView(input)
            .setPositiveButton("确定", (d, w) -> {
                entity.name = input.getText().toString().trim();
                db.formationDao().update(entity);
                refreshSavedList();
                Toast.makeText(getContext(), "已改名", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void deleteFormation(FormationEntity entity) {
        new AlertDialog.Builder(requireContext())
            .setTitle("确认删除")
            .setMessage("确定删除 \"" + entity.name + "\" 吗？")
            .setPositiveButton("删除", (d, w) -> {
                db.formationDao().delete(entity);
                refreshSavedList();
                Toast.makeText(getContext(), "已删除", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("取消", null)
            .show();
    }
}
