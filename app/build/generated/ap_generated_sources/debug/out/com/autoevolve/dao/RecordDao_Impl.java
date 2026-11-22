package com.autoevolve.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.autoevolve.entity.RecordEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RecordDao_Impl implements RecordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecordEntity> __insertionAdapterOfRecordEntity;

  public RecordDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecordEntity = new EntityInsertionAdapter<RecordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `record` (`id`,`timestamp`,`myFormationId`,`enemyFormationId`,`result`,`rounds`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RecordEntity entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.timestamp);
        statement.bindLong(3, entity.myFormationId);
        statement.bindLong(4, entity.enemyFormationId);
        statement.bindLong(5, entity.result);
        statement.bindLong(6, entity.rounds);
      }
    };
  }

  @Override
  public void insert(final RecordEntity record) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRecordEntity.insert(record);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<RecordEntity> getAll() {
    final String _sql = "SELECT * FROM record";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfMyFormationId = CursorUtil.getColumnIndexOrThrow(_cursor, "myFormationId");
      final int _cursorIndexOfEnemyFormationId = CursorUtil.getColumnIndexOrThrow(_cursor, "enemyFormationId");
      final int _cursorIndexOfResult = CursorUtil.getColumnIndexOrThrow(_cursor, "result");
      final int _cursorIndexOfRounds = CursorUtil.getColumnIndexOrThrow(_cursor, "rounds");
      final List<RecordEntity> _result = new ArrayList<RecordEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RecordEntity _item;
        _item = new RecordEntity();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        _item.timestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.myFormationId = _cursor.getInt(_cursorIndexOfMyFormationId);
        _item.enemyFormationId = _cursor.getInt(_cursorIndexOfEnemyFormationId);
        _item.result = _cursor.getInt(_cursorIndexOfResult);
        _item.rounds = _cursor.getInt(_cursorIndexOfRounds);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getWinCount(final int fid) {
    final String _sql = "SELECT COUNT(*) FROM record WHERE result = 1 AND myFormationId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, fid);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getTotalCount(final int fid) {
    final String _sql = "SELECT COUNT(*) FROM record WHERE myFormationId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, fid);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
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
