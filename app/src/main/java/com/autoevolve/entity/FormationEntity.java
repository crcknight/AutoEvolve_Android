package com.autoevolve.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "formation")
public class FormationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String slotsJson;
    public int totalCost;
    public int isFavorite;
}
