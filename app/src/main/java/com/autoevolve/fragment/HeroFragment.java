package com.autoevolve.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.autoevolve.AppDatabase;
import com.autoevolve.R;
import com.autoevolve.entity.HeroEntity;
import com.autoevolve.entity.SynergyEntity;
import java.util.ArrayList;
import java.util.List;

public class HeroFragment extends Fragment {
    private List<HeroEntity> heroes;
    private List<SynergyEntity> synergies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hero, container, false);
        ListView listView = view.findViewById(R.id.list_heroes);
        AppDatabase db = AppDatabase.getInstance(requireContext());
        heroes = db.heroDao().getAll();
        synergies = db.synergyDao().getAll();

        List<String> items = new ArrayList<>();
        for (HeroEntity h : heroes) {
            items.add(h.name + "\n费用:" + h.cost + " 血量:" + h.hp + " 攻击:" + h.atk + " 攻速:" + h.atkSpd);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.item_hero, R.id.tv_hero_name, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, v, position, id) -> showHeroDetail(heroes.get(position)));
        return view;
    }

    private void showHeroDetail(HeroEntity hero) {
        String synergyName = "无";
        if (hero.synergyId >= 0 && hero.synergyId < synergies.size()) {
            synergyName = synergies.get(hero.synergyId).name;
        }
        String msg = "名称: " + hero.name + "\n"
                + "费用: " + hero.cost + "\n"
                + "血量: " + hero.hp + "\n"
                + "攻击: " + hero.atk + "\n"
                + "攻速: " + hero.atkSpd + "\n"
                + "所属羁绊: " + synergyName + "\n"
                + "技能: " + (hero.skillDesc != null ? hero.skillDesc : "暂无");
        new AlertDialog.Builder(requireContext())
                .setTitle("英雄详情")
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .show();
    }
}
