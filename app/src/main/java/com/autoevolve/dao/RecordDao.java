package com.autoevolve.dao;

import androidx.room.*;
import com.autoevolve.entity.RecordEntity;
import java.util.List;

@Dao
public interface RecordDao {
    @Insert void insert(RecordEntity record);
    @Query("SELECT * FROM record") List<RecordEntity> getAll();
    @Query("SELECT COUNT(*) FROM record WHERE result = 1 AND myFormationId = :fid") int getWinCount(int fid);
    @Query("SELECT COUNT(*) FROM record WHERE myFormationId = :fid") int getTotalCount(int fid);
}
