package com.autoevolve;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.autoevolve.dao.*;
import com.autoevolve.entity.*;

@Database(entities = {HeroEntity.class, FormationEntity.class, RecordEntity.class, SynergyEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract HeroDao heroDao();
    public abstract FormationDao formationDao();
    public abstract RecordDao recordDao();
    public abstract SynergyDao synergyDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "autoevolve_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
