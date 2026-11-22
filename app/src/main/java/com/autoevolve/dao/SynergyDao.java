package com.autoevolve.dao;

import androidx.room.*;
import com.autoevolve.entity.SynergyEntity;
import java.util.List;

@Dao
public interface SynergyDao {
    @Insert void insert(SynergyEntity synergy);
    @Query("SELECT * FROM synergy") List<SynergyEntity> getAll();
}
