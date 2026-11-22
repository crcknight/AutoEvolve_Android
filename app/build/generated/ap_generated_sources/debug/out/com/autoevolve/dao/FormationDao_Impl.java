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
import com.autoevolve.entity.FormationEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FormationDao_Impl implements FormationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FormationEntity> __insertionAdapterOfFormationEntity;

  private final EntityDeletionOrUpdateAdapter<FormationEntity> __deletionAdapterOfFormationEntity;

  private final EntityDeletionOrUpdateAdapter<FormationEntity> __updateAdapterOfFormationEntity;

  public FormationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFormationEntity = new EntityInsertionAdapter<FormationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `formation` (`id`,`name`,`slotsJson`,`totalCost`,`isFavorite`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final FormationEntity entity) {
        statement.bindLong(1, entity.id);
        if (entity.name == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.name);
        }
        if (entity.slotsJson == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.slotsJson);
        }
        statement.bindLong(4, entity.totalCost);
        statement.bindLong(5, entity.isFavorite);
      }
    };
    this.__deletionAdapterOfFormationEntity = new EntityDeletionOrUpdateAdapter<FormationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `formation` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final FormationEntity entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfFormationEntity = new EntityDeletionOrUpdateAdapter<FormationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `formation` SET `id` = ?,`name` = ?,`slotsJson` = ?,`totalCost` = ?,`isFavorite` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final FormationEntity entity) {
        statement.bindLong(1, entity.id);
        if (entity.name == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.name);
        }
        if (entity.slotsJson == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.slotsJson);
        }
        statement.bindLong(4, entity.totalCost);
        statement.bindLong(5, entity.isFavorite);
        statement.bindLong(6, entity.id);
      }
    };
  }

  @Override
  public void insert(final FormationEntity formation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFormationEntity.insert(formation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final FormationEntity formation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFormationEntity.handle(formation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final FormationEntity formation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfFormationEntity.handle(formation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<FormationEntity> getAll() {
    final String _sql = "SELECT * FROM formation";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfSlotsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "slotsJson");
      final int _cursorIndexOfTotalCost = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCost");
      final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
      final List<FormationEntity> _result = new ArrayList<FormationEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final FormationEntity _item;
        _item = new FormationEntity();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfName)) {
          _item.name = null;
        } else {
          _item.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfSlotsJson)) {
          _item.slotsJson = null;
        } else {
          _item.slotsJson = _cursor.getString(_cursorIndexOfSlotsJson);
        }
        _item.totalCost = _cursor.getInt(_cursorIndexOfTotalCost);
        _item.isFavorite = _cursor.getInt(_cursorIndexOfIsFavorite);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public FormationEntity getById(final int id) {
    final String _sql = "SELECT * FROM formation WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfSlotsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "slotsJson");
      final int _cursorIndexOfTotalCost = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCost");
      final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
      final FormationEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new FormationEntity();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfName)) {
          _result.name = null;
        } else {
          _result.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfSlotsJson)) {
          _result.slotsJson = null;
        } else {
          _result.slotsJson = _cursor.getString(_cursorIndexOfSlotsJson);
        }
        _result.totalCost = _cursor.getInt(_cursorIndexOfTotalCost);
        _result.isFavorite = _cursor.getInt(_cursorIndexOfIsFavorite);
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
