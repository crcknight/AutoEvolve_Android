package com.autoevolve;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.autoevolve.dao.FormationDao;
import com.autoevolve.dao.FormationDao_Impl;
import com.autoevolve.dao.HeroDao;
import com.autoevolve.dao.HeroDao_Impl;
import com.autoevolve.dao.RecordDao;
import com.autoevolve.dao.RecordDao_Impl;
import com.autoevolve.dao.SynergyDao;
import com.autoevolve.dao.SynergyDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile HeroDao _heroDao;

  private volatile FormationDao _formationDao;

  private volatile RecordDao _recordDao;

  private volatile SynergyDao _synergyDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `hero` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `cost` INTEGER NOT NULL, `hp` INTEGER NOT NULL, `atk` INTEGER NOT NULL, `atkSpd` REAL NOT NULL, `skillDesc` TEXT, `synergyId` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `formation` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `slotsJson` TEXT, `totalCost` INTEGER NOT NULL, `isFavorite` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `record` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `myFormationId` INTEGER NOT NULL, `enemyFormationId` INTEGER NOT NULL, `result` INTEGER NOT NULL, `rounds` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `synergy` (`id` INTEGER NOT NULL, `name` TEXT, `requiredCount` INTEGER NOT NULL, `atkMultiplier` REAL NOT NULL, `atkSpdMultiplier` REAL NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f748740d44b87ddeddfa0f7aa8bcd25b')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `hero`");
        db.execSQL("DROP TABLE IF EXISTS `formation`");
        db.execSQL("DROP TABLE IF EXISTS `record`");
        db.execSQL("DROP TABLE IF EXISTS `synergy`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsHero = new HashMap<String, TableInfo.Column>(8);
        _columnsHero.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHero.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHero.put("cost", new TableInfo.Column("cost", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHero.put("hp", new TableInfo.Column("hp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHero.put("atk", new TableInfo.Column("atk", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHero.put("atkSpd", new TableInfo.Column("atkSpd", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHero.put("skillDesc", new TableInfo.Column("skillDesc", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHero.put("synergyId", new TableInfo.Column("synergyId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHero = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHero = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHero = new TableInfo("hero", _columnsHero, _foreignKeysHero, _indicesHero);
        final TableInfo _existingHero = TableInfo.read(db, "hero");
        if (!_infoHero.equals(_existingHero)) {
          return new RoomOpenHelper.ValidationResult(false, "hero(com.autoevolve.entity.HeroEntity).\n"
                  + " Expected:\n" + _infoHero + "\n"
                  + " Found:\n" + _existingHero);
        }
        final HashMap<String, TableInfo.Column> _columnsFormation = new HashMap<String, TableInfo.Column>(5);
        _columnsFormation.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormation.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormation.put("slotsJson", new TableInfo.Column("slotsJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormation.put("totalCost", new TableInfo.Column("totalCost", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFormation.put("isFavorite", new TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFormation = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFormation = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFormation = new TableInfo("formation", _columnsFormation, _foreignKeysFormation, _indicesFormation);
        final TableInfo _existingFormation = TableInfo.read(db, "formation");
        if (!_infoFormation.equals(_existingFormation)) {
          return new RoomOpenHelper.ValidationResult(false, "formation(com.autoevolve.entity.FormationEntity).\n"
                  + " Expected:\n" + _infoFormation + "\n"
                  + " Found:\n" + _existingFormation);
        }
        final HashMap<String, TableInfo.Column> _columnsRecord = new HashMap<String, TableInfo.Column>(6);
        _columnsRecord.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecord.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecord.put("myFormationId", new TableInfo.Column("myFormationId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecord.put("enemyFormationId", new TableInfo.Column("enemyFormationId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecord.put("result", new TableInfo.Column("result", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecord.put("rounds", new TableInfo.Column("rounds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecord = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecord = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecord = new TableInfo("record", _columnsRecord, _foreignKeysRecord, _indicesRecord);
        final TableInfo _existingRecord = TableInfo.read(db, "record");
        if (!_infoRecord.equals(_existingRecord)) {
          return new RoomOpenHelper.ValidationResult(false, "record(com.autoevolve.entity.RecordEntity).\n"
                  + " Expected:\n" + _infoRecord + "\n"
                  + " Found:\n" + _existingRecord);
        }
        final HashMap<String, TableInfo.Column> _columnsSynergy = new HashMap<String, TableInfo.Column>(5);
        _columnsSynergy.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSynergy.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSynergy.put("requiredCount", new TableInfo.Column("requiredCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSynergy.put("atkMultiplier", new TableInfo.Column("atkMultiplier", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSynergy.put("atkSpdMultiplier", new TableInfo.Column("atkSpdMultiplier", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSynergy = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSynergy = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSynergy = new TableInfo("synergy", _columnsSynergy, _foreignKeysSynergy, _indicesSynergy);
        final TableInfo _existingSynergy = TableInfo.read(db, "synergy");
        if (!_infoSynergy.equals(_existingSynergy)) {
          return new RoomOpenHelper.ValidationResult(false, "synergy(com.autoevolve.entity.SynergyEntity).\n"
                  + " Expected:\n" + _infoSynergy + "\n"
                  + " Found:\n" + _existingSynergy);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "f748740d44b87ddeddfa0f7aa8bcd25b", "b2ca283eb1007dc4643b6dbc53fc79ee");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "hero","formation","record","synergy");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `hero`");
      _db.execSQL("DELETE FROM `formation`");
      _db.execSQL("DELETE FROM `record`");
      _db.execSQL("DELETE FROM `synergy`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(HeroDao.class, HeroDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FormationDao.class, FormationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecordDao.class, RecordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SynergyDao.class, SynergyDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public HeroDao heroDao() {
    if (_heroDao != null) {
      return _heroDao;
    } else {
      synchronized(this) {
        if(_heroDao == null) {
          _heroDao = new HeroDao_Impl(this);
        }
        return _heroDao;
      }
    }
  }

  @Override
  public FormationDao formationDao() {
    if (_formationDao != null) {
      return _formationDao;
    } else {
      synchronized(this) {
        if(_formationDao == null) {
          _formationDao = new FormationDao_Impl(this);
        }
        return _formationDao;
      }
    }
  }

  @Override
  public RecordDao recordDao() {
    if (_recordDao != null) {
      return _recordDao;
    } else {
      synchronized(this) {
        if(_recordDao == null) {
          _recordDao = new RecordDao_Impl(this);
        }
        return _recordDao;
      }
    }
  }

  @Override
  public SynergyDao synergyDao() {
    if (_synergyDao != null) {
      return _synergyDao;
    } else {
      synchronized(this) {
        if(_synergyDao == null) {
          _synergyDao = new SynergyDao_Impl(this);
        }
        return _synergyDao;
      }
    }
  }
}
