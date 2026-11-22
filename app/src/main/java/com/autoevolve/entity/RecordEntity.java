package com.autoevolve.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "record")
public class RecordEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public long timestamp;
    public int myFormationId;
    public int enemyFormationId;
    public int result;
    public int rounds;
}
