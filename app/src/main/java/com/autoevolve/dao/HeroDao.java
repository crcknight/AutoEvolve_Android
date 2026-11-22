package com.autoevolve.dao;

import androidx.room.*;
import com.autoevolve.entity.HeroEntity;
import java.util.List;

@Dao
public interface HeroDao {
    @Insert void insert(HeroEntity hero);
    @Update void update(HeroEntity hero);
    @Delete void delete(HeroEntity hero);
    @Query("SELECT * FROM hero") List<HeroEntity> getAll();
    @Query("SELECT * FROM hero WHERE id = :id") HeroEntity getById(int id);
}
