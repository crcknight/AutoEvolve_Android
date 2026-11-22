package com.autoevolve.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "synergy")
public class SynergyEntity {
    @PrimaryKey
    public int id;
    public String name;
    public int requiredCount;
    public double atkMultiplier;
    public double atkSpdMultiplier;
}
