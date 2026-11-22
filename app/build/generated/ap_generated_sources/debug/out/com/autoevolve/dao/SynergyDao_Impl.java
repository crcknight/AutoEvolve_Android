package com.autoevolve.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.autoevolve.entity.SynergyEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SynergyDao_Impl implements SynergyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SynergyEntity> __insertionAdapterOfSynergyEntity;

  public SynergyDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSynergyEntity = new EntityInsertionAdapter<SynergyEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `synergy` (`id`,`name`,`requiredCount`,`atkMultiplier`,`atkSpdMultiplier`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final SynergyEntity entity) {
        statement.bindLong(1, entity.id);
        if (entity.name == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.name);
        }
        statement.bindLong(3, entity.requiredCount);
        statement.bindDouble(4, entity.atkMultiplier);
        statement.bindDouble(5, entity.atkSpdMultiplier);
      }
    };
  }

  @Override
  public void insert(final SynergyEntity synergy) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSynergyEntity.insert(synergy);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<SynergyEntity> getAll() {
    final String _sql = "SELECT * FROM synergy";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfRequiredCount = CursorUtil.getColumnIndexOrThrow(_cursor, "requiredCount");
      final int _cursorIndexOfAtkMultiplier = CursorUtil.getColumnIndexOrThrow(_cursor, "atkMultiplier");
      final int _cursorIndexOfAtkSpdMultiplier = CursorUtil.getColumnIndexOrThrow(_cursor, "atkSpdMultiplier");
      final List<SynergyEntity> _result = new ArrayList<SynergyEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SynergyEntity _item;
        _item = new SynergyEntity();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfName)) {
          _item.name = null;
        } else {
          _item.name = _cursor.getString(_cursorIndexOfName);
        }
        _item.requiredCount = _cursor.getInt(_cursorIndexOfRequiredCount);
        _item.atkMultiplier = _cursor.getDouble(_cursorIndexOfAtkMultiplier);
        _item.atkSpdMultiplier = _cursor.getDouble(_cursorIndexOfAtkSpdMultiplier);
        _result.add(_item);
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
