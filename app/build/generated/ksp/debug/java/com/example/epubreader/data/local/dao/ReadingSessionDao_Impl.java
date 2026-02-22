package com.example.epubreader.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.epubreader.data.local.entity.ReadingSession;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReadingSessionDao_Impl implements ReadingSessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReadingSession> __insertionAdapterOfReadingSession;

  private final EntityDeletionOrUpdateAdapter<ReadingSession> __deletionAdapterOfReadingSession;

  private final EntityDeletionOrUpdateAdapter<ReadingSession> __updateAdapterOfReadingSession;

  private final SharedSQLiteStatement __preparedStmtOfCloseSession;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSessionsForBook;

  public ReadingSessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReadingSession = new EntityInsertionAdapter<ReadingSession>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reading_sessions` (`id`,`bookId`,`sessionStart`,`sessionEnd`,`durationMillis`,`pagesRead`,`startPosition`,`endPosition`,`isActive`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReadingSession entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBookId());
        statement.bindLong(3, entity.getSessionStart());
        if (entity.getSessionEnd() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getSessionEnd());
        }
        statement.bindLong(5, entity.getDurationMillis());
        statement.bindLong(6, entity.getPagesRead());
        statement.bindLong(7, entity.getStartPosition());
        statement.bindLong(8, entity.getEndPosition());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__deletionAdapterOfReadingSession = new EntityDeletionOrUpdateAdapter<ReadingSession>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `reading_sessions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReadingSession entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfReadingSession = new EntityDeletionOrUpdateAdapter<ReadingSession>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reading_sessions` SET `id` = ?,`bookId` = ?,`sessionStart` = ?,`sessionEnd` = ?,`durationMillis` = ?,`pagesRead` = ?,`startPosition` = ?,`endPosition` = ?,`isActive` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReadingSession entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBookId());
        statement.bindLong(3, entity.getSessionStart());
        if (entity.getSessionEnd() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getSessionEnd());
        }
        statement.bindLong(5, entity.getDurationMillis());
        statement.bindLong(6, entity.getPagesRead());
        statement.bindLong(7, entity.getStartPosition());
        statement.bindLong(8, entity.getEndPosition());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfCloseSession = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE reading_sessions \n"
                + "        SET sessionEnd = ?,\n"
                + "            durationMillis = ?,\n"
                + "            endPosition = ?,\n"
                + "            pagesRead = ?,\n"
                + "            isActive = 0\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSessionsForBook = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reading_sessions WHERE bookId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSession(final ReadingSession session,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfReadingSession.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSession(final ReadingSession session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfReadingSession.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSession(final ReadingSession session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfReadingSession.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object closeSession(final long sessionId, final long endTime, final long duration,
      final int endPosition, final int pagesRead, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCloseSession.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, endTime);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, duration);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, endPosition);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, pagesRead);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, sessionId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfCloseSession.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSessionsForBook(final long bookId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSessionsForBook.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, bookId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteSessionsForBook.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReadingSession>> getSessionsForBook(final long bookId) {
    final String _sql = "SELECT * FROM reading_sessions WHERE bookId = ? ORDER BY sessionStart DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reading_sessions"}, new Callable<List<ReadingSession>>() {
      @Override
      @NonNull
      public List<ReadingSession> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSessionStart = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionStart");
          final int _cursorIndexOfSessionEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionEnd");
          final int _cursorIndexOfDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMillis");
          final int _cursorIndexOfPagesRead = CursorUtil.getColumnIndexOrThrow(_cursor, "pagesRead");
          final int _cursorIndexOfStartPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "startPosition");
          final int _cursorIndexOfEndPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "endPosition");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<ReadingSession> _result = new ArrayList<ReadingSession>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReadingSession _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpSessionStart;
            _tmpSessionStart = _cursor.getLong(_cursorIndexOfSessionStart);
            final Long _tmpSessionEnd;
            if (_cursor.isNull(_cursorIndexOfSessionEnd)) {
              _tmpSessionEnd = null;
            } else {
              _tmpSessionEnd = _cursor.getLong(_cursorIndexOfSessionEnd);
            }
            final long _tmpDurationMillis;
            _tmpDurationMillis = _cursor.getLong(_cursorIndexOfDurationMillis);
            final int _tmpPagesRead;
            _tmpPagesRead = _cursor.getInt(_cursorIndexOfPagesRead);
            final int _tmpStartPosition;
            _tmpStartPosition = _cursor.getInt(_cursorIndexOfStartPosition);
            final int _tmpEndPosition;
            _tmpEndPosition = _cursor.getInt(_cursorIndexOfEndPosition);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new ReadingSession(_tmpId,_tmpBookId,_tmpSessionStart,_tmpSessionEnd,_tmpDurationMillis,_tmpPagesRead,_tmpStartPosition,_tmpEndPosition,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getSessionsForBookSync(final long bookId,
      final Continuation<? super List<ReadingSession>> $completion) {
    final String _sql = "SELECT * FROM reading_sessions WHERE bookId = ? ORDER BY sessionStart DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReadingSession>>() {
      @Override
      @NonNull
      public List<ReadingSession> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSessionStart = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionStart");
          final int _cursorIndexOfSessionEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionEnd");
          final int _cursorIndexOfDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMillis");
          final int _cursorIndexOfPagesRead = CursorUtil.getColumnIndexOrThrow(_cursor, "pagesRead");
          final int _cursorIndexOfStartPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "startPosition");
          final int _cursorIndexOfEndPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "endPosition");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<ReadingSession> _result = new ArrayList<ReadingSession>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReadingSession _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpSessionStart;
            _tmpSessionStart = _cursor.getLong(_cursorIndexOfSessionStart);
            final Long _tmpSessionEnd;
            if (_cursor.isNull(_cursorIndexOfSessionEnd)) {
              _tmpSessionEnd = null;
            } else {
              _tmpSessionEnd = _cursor.getLong(_cursorIndexOfSessionEnd);
            }
            final long _tmpDurationMillis;
            _tmpDurationMillis = _cursor.getLong(_cursorIndexOfDurationMillis);
            final int _tmpPagesRead;
            _tmpPagesRead = _cursor.getInt(_cursorIndexOfPagesRead);
            final int _tmpStartPosition;
            _tmpStartPosition = _cursor.getInt(_cursorIndexOfStartPosition);
            final int _tmpEndPosition;
            _tmpEndPosition = _cursor.getInt(_cursorIndexOfEndPosition);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new ReadingSession(_tmpId,_tmpBookId,_tmpSessionStart,_tmpSessionEnd,_tmpDurationMillis,_tmpPagesRead,_tmpStartPosition,_tmpEndPosition,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getActiveSession(final long bookId,
      final Continuation<? super ReadingSession> $completion) {
    final String _sql = "SELECT * FROM reading_sessions WHERE isActive = 1 AND bookId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReadingSession>() {
      @Override
      @Nullable
      public ReadingSession call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSessionStart = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionStart");
          final int _cursorIndexOfSessionEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionEnd");
          final int _cursorIndexOfDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMillis");
          final int _cursorIndexOfPagesRead = CursorUtil.getColumnIndexOrThrow(_cursor, "pagesRead");
          final int _cursorIndexOfStartPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "startPosition");
          final int _cursorIndexOfEndPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "endPosition");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final ReadingSession _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpSessionStart;
            _tmpSessionStart = _cursor.getLong(_cursorIndexOfSessionStart);
            final Long _tmpSessionEnd;
            if (_cursor.isNull(_cursorIndexOfSessionEnd)) {
              _tmpSessionEnd = null;
            } else {
              _tmpSessionEnd = _cursor.getLong(_cursorIndexOfSessionEnd);
            }
            final long _tmpDurationMillis;
            _tmpDurationMillis = _cursor.getLong(_cursorIndexOfDurationMillis);
            final int _tmpPagesRead;
            _tmpPagesRead = _cursor.getInt(_cursorIndexOfPagesRead);
            final int _tmpStartPosition;
            _tmpStartPosition = _cursor.getInt(_cursorIndexOfStartPosition);
            final int _tmpEndPosition;
            _tmpEndPosition = _cursor.getInt(_cursorIndexOfEndPosition);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new ReadingSession(_tmpId,_tmpBookId,_tmpSessionStart,_tmpSessionEnd,_tmpDurationMillis,_tmpPagesRead,_tmpStartPosition,_tmpEndPosition,_tmpIsActive);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSessionsInDateRange(final long startDate, final long endDate,
      final Continuation<? super List<ReadingSession>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM reading_sessions \n"
            + "        WHERE sessionStart >= ? AND sessionStart <= ?\n"
            + "        ORDER BY sessionStart DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDate);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReadingSession>>() {
      @Override
      @NonNull
      public List<ReadingSession> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSessionStart = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionStart");
          final int _cursorIndexOfSessionEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionEnd");
          final int _cursorIndexOfDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMillis");
          final int _cursorIndexOfPagesRead = CursorUtil.getColumnIndexOrThrow(_cursor, "pagesRead");
          final int _cursorIndexOfStartPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "startPosition");
          final int _cursorIndexOfEndPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "endPosition");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<ReadingSession> _result = new ArrayList<ReadingSession>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReadingSession _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpSessionStart;
            _tmpSessionStart = _cursor.getLong(_cursorIndexOfSessionStart);
            final Long _tmpSessionEnd;
            if (_cursor.isNull(_cursorIndexOfSessionEnd)) {
              _tmpSessionEnd = null;
            } else {
              _tmpSessionEnd = _cursor.getLong(_cursorIndexOfSessionEnd);
            }
            final long _tmpDurationMillis;
            _tmpDurationMillis = _cursor.getLong(_cursorIndexOfDurationMillis);
            final int _tmpPagesRead;
            _tmpPagesRead = _cursor.getInt(_cursorIndexOfPagesRead);
            final int _tmpStartPosition;
            _tmpStartPosition = _cursor.getInt(_cursorIndexOfStartPosition);
            final int _tmpEndPosition;
            _tmpEndPosition = _cursor.getInt(_cursorIndexOfEndPosition);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new ReadingSession(_tmpId,_tmpBookId,_tmpSessionStart,_tmpSessionEnd,_tmpDurationMillis,_tmpPagesRead,_tmpStartPosition,_tmpEndPosition,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReadingSession>> getTodaySessions(final long todayStart) {
    final String _sql = "\n"
            + "        SELECT * FROM reading_sessions \n"
            + "        WHERE sessionStart >= ?\n"
            + "        ORDER BY sessionStart DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, todayStart);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reading_sessions"}, new Callable<List<ReadingSession>>() {
      @Override
      @NonNull
      public List<ReadingSession> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSessionStart = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionStart");
          final int _cursorIndexOfSessionEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionEnd");
          final int _cursorIndexOfDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMillis");
          final int _cursorIndexOfPagesRead = CursorUtil.getColumnIndexOrThrow(_cursor, "pagesRead");
          final int _cursorIndexOfStartPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "startPosition");
          final int _cursorIndexOfEndPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "endPosition");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<ReadingSession> _result = new ArrayList<ReadingSession>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReadingSession _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpSessionStart;
            _tmpSessionStart = _cursor.getLong(_cursorIndexOfSessionStart);
            final Long _tmpSessionEnd;
            if (_cursor.isNull(_cursorIndexOfSessionEnd)) {
              _tmpSessionEnd = null;
            } else {
              _tmpSessionEnd = _cursor.getLong(_cursorIndexOfSessionEnd);
            }
            final long _tmpDurationMillis;
            _tmpDurationMillis = _cursor.getLong(_cursorIndexOfDurationMillis);
            final int _tmpPagesRead;
            _tmpPagesRead = _cursor.getInt(_cursorIndexOfPagesRead);
            final int _tmpStartPosition;
            _tmpStartPosition = _cursor.getInt(_cursorIndexOfStartPosition);
            final int _tmpEndPosition;
            _tmpEndPosition = _cursor.getInt(_cursorIndexOfEndPosition);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new ReadingSession(_tmpId,_tmpBookId,_tmpSessionStart,_tmpSessionEnd,_tmpDurationMillis,_tmpPagesRead,_tmpStartPosition,_tmpEndPosition,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalReadingTimeForBook(final long bookId,
      final Continuation<? super Long> $completion) {
    final String _sql = "SELECT SUM(durationMillis) FROM reading_sessions WHERE bookId = ? AND isActive = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalReadingTime(final Continuation<? super Long> $completion) {
    final String _sql = "SELECT SUM(durationMillis) FROM reading_sessions WHERE isActive = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTodayReadingTime(final long todayStart,
      final Continuation<? super Long> $completion) {
    final String _sql = "\n"
            + "        SELECT SUM(durationMillis) FROM reading_sessions \n"
            + "        WHERE sessionStart >= ? AND isActive = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, todayStart);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMonthReadingTime(final long monthStart,
      final Continuation<? super Long> $completion) {
    final String _sql = "\n"
            + "        SELECT SUM(durationMillis) FROM reading_sessions \n"
            + "        WHERE sessionStart >= ? AND isActive = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, monthStart);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSessionCountForBook(final long bookId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM reading_sessions WHERE bookId = ? AND isActive = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLastCompletedSession(final long bookId,
      final Continuation<? super ReadingSession> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM reading_sessions \n"
            + "        WHERE bookId = ? AND isActive = 0 \n"
            + "        ORDER BY sessionEnd DESC \n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReadingSession>() {
      @Override
      @Nullable
      public ReadingSession call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSessionStart = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionStart");
          final int _cursorIndexOfSessionEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionEnd");
          final int _cursorIndexOfDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMillis");
          final int _cursorIndexOfPagesRead = CursorUtil.getColumnIndexOrThrow(_cursor, "pagesRead");
          final int _cursorIndexOfStartPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "startPosition");
          final int _cursorIndexOfEndPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "endPosition");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final ReadingSession _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpSessionStart;
            _tmpSessionStart = _cursor.getLong(_cursorIndexOfSessionStart);
            final Long _tmpSessionEnd;
            if (_cursor.isNull(_cursorIndexOfSessionEnd)) {
              _tmpSessionEnd = null;
            } else {
              _tmpSessionEnd = _cursor.getLong(_cursorIndexOfSessionEnd);
            }
            final long _tmpDurationMillis;
            _tmpDurationMillis = _cursor.getLong(_cursorIndexOfDurationMillis);
            final int _tmpPagesRead;
            _tmpPagesRead = _cursor.getInt(_cursorIndexOfPagesRead);
            final int _tmpStartPosition;
            _tmpStartPosition = _cursor.getInt(_cursorIndexOfStartPosition);
            final int _tmpEndPosition;
            _tmpEndPosition = _cursor.getInt(_cursorIndexOfEndPosition);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new ReadingSession(_tmpId,_tmpBookId,_tmpSessionStart,_tmpSessionEnd,_tmpDurationMillis,_tmpPagesRead,_tmpStartPosition,_tmpEndPosition,_tmpIsActive);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
