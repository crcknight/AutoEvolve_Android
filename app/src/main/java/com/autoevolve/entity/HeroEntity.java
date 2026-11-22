package com.autoevolve.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hero")
public class HeroEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int cost;
    public int hp;
    public int atk;
    public double atkSpd;
    public String skillDesc;
    public int synergyId;
}
