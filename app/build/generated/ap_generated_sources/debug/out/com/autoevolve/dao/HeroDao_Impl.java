package com.autoevolve.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.autoevolve.entity.HeroEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class HeroDao_Impl implements HeroDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HeroEntity> __insertionAdapterOfHeroEntity;

  private final EntityDeletionOrUpdateAdapter<HeroEntity> __deletionAdapterOfHeroEntity;

  private final EntityDeletionOrUpdateAdapter<HeroEntity> __updateAdapterOfHeroEntity;

  public HeroDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHeroEntity = new EntityInsertionAdapter<HeroEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `hero` (`id`,`name`,`cost`,`hp`,`atk`,`atkSpd`,`skillDesc`,`synergyId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final HeroEntity entity) {
        statement.bindLong(1, entity.id);
        if (entity.name == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.name);
        }
        statement.bindLong(3, entity.cost);
        statement.bindLong(4, entity.hp);
        statement.bindLong(5, entity.atk);
        statement.bindDouble(6, entity.atkSpd);
        if (entity.skillDesc == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.skillDesc);
        }
        statement.bindLong(8, entity.synergyId);
      }
    };
    this.__deletionAdapterOfHeroEntity = new EntityDeletionOrUpdateAdapter<HeroEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `hero` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final HeroEntity entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfHeroEntity = new EntityDeletionOrUpdateAdapter<HeroEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `hero` SET `id` = ?,`name` = ?,`cost` = ?,`hp` = ?,`atk` = ?,`atkSpd` = ?,`skillDesc` = ?,`synergyId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final HeroEntity entity) {
        statement.bindLong(1, entity.id);
        if (entity.name == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.name);
        }
        statement.bindLong(3, entity.cost);
        statement.bindLong(4, entity.hp);
        statement.bindLong(5, entity.atk);
        statement.bindDouble(6, entity.atkSpd);
        if (entity.skillDesc == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.skillDesc);
        }
        statement.bindLong(8, entity.synergyId);
        statement.bindLong(9, entity.id);
      }
    };
  }

  @Override
  public void insert(final HeroEntity hero) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHeroEntity.insert(hero);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final HeroEntity hero) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfHeroEntity.handle(hero);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final HeroEntity hero) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfHeroEntity.handle(hero);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<HeroEntity> getAll() {
    final String _sql = "SELECT * FROM hero";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCost = CursorUtil.getColumnIndexOrThrow(_cursor, "cost");
      final int _cursorIndexOfHp = CursorUtil.getColumnIndexOrThrow(_cursor, "hp");
      final int _cursorIndexOfAtk = CursorUtil.getColumnIndexOrThrow(_cursor, "atk");
      final int _cursorIndexOfAtkSpd = CursorUtil.getColumnIndexOrThrow(_cursor, "atkSpd");
      final int _cursorIndexOfSkillDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "skillDesc");
      final int _cursorIndexOfSynergyId = CursorUtil.getColumnIndexOrThrow(_cursor, "synergyId");
      final List<HeroEntity> _result = new ArrayList<HeroEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final HeroEntity _item;
        _item = new HeroEntity();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfName)) {
          _item.name = null;
        } else {
          _item.name = _cursor.getString(_cursorIndexOfName);
        }
        _item.cost = _cursor.getInt(_cursorIndexOfCost);
        _item.hp = _cursor.getInt(_cursorIndexOfHp);
        _item.atk = _cursor.getInt(_cursorIndexOfAtk);
        _item.atkSpd = _cursor.getDouble(_cursorIndexOfAtkSpd);
        if (_cursor.isNull(_cursorIndexOfSkillDesc)) {
          _item.skillDesc = null;
        } else {
          _item.skillDesc = _cursor.getString(_cursorIndexOfSkillDesc);
        }
        _item.synergyId = _cursor.getInt(_cursorIndexOfSynergyId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public HeroEntity getById(final int id) {
    final String _sql = "SELECT * FROM hero WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCost = CursorUtil.getColumnIndexOrThrow(_cursor, "cost");
      final int _cursorIndexOfHp = CursorUtil.getColumnIndexOrThrow(_cursor, "hp");
      final int _cursorIndexOfAtk = CursorUtil.getColumnIndexOrThrow(_cursor, "atk");
      final int _cursorIndexOfAtkSpd = CursorUtil.getColumnIndexOrThrow(_cursor, "atkSpd");
      final int _cursorIndexOfSkillDesc = CursorUtil.getColumnIndexOrThrow(_cursor, "skillDesc");
      final int _cursorIndexOfSynergyId = CursorUtil.getColumnIndexOrThrow(_cursor, "synergyId");
      final HeroEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new HeroEntity();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfName)) {
          _result.name = null;
        } else {
          _result.name = _cursor.getString(_cursorIndexOfName);
        }
        _result.cost = _cursor.getInt(_cursorIndexOfCost);
        _result.hp = _cursor.getInt(_cursorIndexOfHp);
        _result.atk = _cursor.getInt(_cursorIndexOfAtk);
        _result.atkSpd = _cursor.getDouble(_cursorIndexOfAtkSpd);
        if (_cursor.isNull(_cursorIndexOfSkillDesc)) {
          _result.skillDesc = null;
        } else {
          _result.skillDesc = _cursor.getString(_cursorIndexOfSkillDesc);
        }
        _result.synergyId = _cursor.getInt(_cursorIndexOfSynergyId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
