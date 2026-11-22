package com.autoevolve.dao;

import androidx.room.*;
import com.autoevolve.entity.FormationEntity;
import java.util.List;

@Dao
public interface FormationDao {
    @Insert void insert(FormationEntity formation);
    @Update void update(FormationEntity formation);
    @Delete void delete(FormationEntity formation);
    @Query("SELECT * FROM formation") List<FormationEntity> getAll();
    @Query("SELECT * FROM formation WHERE id = :id") FormationEntity getById(int id);
}
