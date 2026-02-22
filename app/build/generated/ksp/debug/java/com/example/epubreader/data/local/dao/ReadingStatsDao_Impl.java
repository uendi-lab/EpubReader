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
import com.example.epubreader.data.local.entity.ReadingStats;
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
public final class ReadingStatsDao_Impl implements ReadingStatsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReadingStats> __insertionAdapterOfReadingStats;

  private final EntityDeletionOrUpdateAdapter<ReadingStats> __deletionAdapterOfReadingStats;

  private final EntityDeletionOrUpdateAdapter<ReadingStats> __updateAdapterOfReadingStats;

  private final SharedSQLiteStatement __preparedStmtOfDeleteStatsForBook;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBookStats;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStreak;

  public ReadingStatsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReadingStats = new EntityInsertionAdapter<ReadingStats>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reading_stats` (`bookId`,`totalReadingTimeMillis`,`totalSessions`,`averageSessionDurationMillis`,`averagePagesPerMinute`,`averageWordsPerMinute`,`completionPercentage`,`daysToComplete`,`lastUpdated`,`longestSessionMillis`,`shortestSessionMillis`,`currentStreak`,`longestStreak`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReadingStats entity) {
        statement.bindLong(1, entity.getBookId());
        statement.bindLong(2, entity.getTotalReadingTimeMillis());
        statement.bindLong(3, entity.getTotalSessions());
        statement.bindLong(4, entity.getAverageSessionDurationMillis());
        statement.bindDouble(5, entity.getAveragePagesPerMinute());
        statement.bindDouble(6, entity.getAverageWordsPerMinute());
        statement.bindDouble(7, entity.getCompletionPercentage());
        if (entity.getDaysToComplete() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getDaysToComplete());
        }
        statement.bindLong(9, entity.getLastUpdated());
        statement.bindLong(10, entity.getLongestSessionMillis());
        statement.bindLong(11, entity.getShortestSessionMillis());
        statement.bindLong(12, entity.getCurrentStreak());
        statement.bindLong(13, entity.getLongestStreak());
      }
    };
    this.__deletionAdapterOfReadingStats = new EntityDeletionOrUpdateAdapter<ReadingStats>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `reading_stats` WHERE `bookId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReadingStats entity) {
        statement.bindLong(1, entity.getBookId());
      }
    };
    this.__updateAdapterOfReadingStats = new EntityDeletionOrUpdateAdapter<ReadingStats>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reading_stats` SET `bookId` = ?,`totalReadingTimeMillis` = ?,`totalSessions` = ?,`averageSessionDurationMillis` = ?,`averagePagesPerMinute` = ?,`averageWordsPerMinute` = ?,`completionPercentage` = ?,`daysToComplete` = ?,`lastUpdated` = ?,`longestSessionMillis` = ?,`shortestSessionMillis` = ?,`currentStreak` = ?,`longestStreak` = ? WHERE `bookId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReadingStats entity) {
        statement.bindLong(1, entity.getBookId());
        statement.bindLong(2, entity.getTotalReadingTimeMillis());
        statement.bindLong(3, entity.getTotalSessions());
        statement.bindLong(4, entity.getAverageSessionDurationMillis());
        statement.bindDouble(5, entity.getAveragePagesPerMinute());
        statement.bindDouble(6, entity.getAverageWordsPerMinute());
        statement.bindDouble(7, entity.getCompletionPercentage());
        if (entity.getDaysToComplete() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getDaysToComplete());
        }
        statement.bindLong(9, entity.getLastUpdated());
        statement.bindLong(10, entity.getLongestSessionMillis());
        statement.bindLong(11, entity.getShortestSessionMillis());
        statement.bindLong(12, entity.getCurrentStreak());
        statement.bindLong(13, entity.getLongestStreak());
        statement.bindLong(14, entity.getBookId());
      }
    };
    this.__preparedStmtOfDeleteStatsForBook = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reading_stats WHERE bookId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBookStats = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE reading_stats \n"
                + "        SET totalReadingTimeMillis = ?,\n"
                + "            totalSessions = ?,\n"
                + "            averageSessionDurationMillis = ?,\n"
                + "            averagePagesPerMinute = ?,\n"
                + "            averageWordsPerMinute = ?,\n"
                + "            completionPercentage = ?,\n"
                + "            daysToComplete = ?,\n"
                + "            longestSessionMillis = ?,\n"
                + "            shortestSessionMillis = ?,\n"
                + "            lastUpdated = ?\n"
                + "        WHERE bookId = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStreak = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE reading_stats \n"
                + "        SET currentStreak = ?,\n"
                + "            longestStreak = CASE WHEN ? > longestStreak \n"
                + "                                  THEN ? \n"
                + "                                  ELSE longestStreak END\n"
                + "        WHERE bookId = ?\n"
                + "    ";
        return _query;
      }
    };
  }

  @Override
  public Object insertStats(final ReadingStats stats,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfReadingStats.insert(stats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteStats(final ReadingStats stats,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfReadingStats.handle(stats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStats(final ReadingStats stats,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfReadingStats.handle(stats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteStatsForBook(final long bookId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteStatsForBook.acquire();
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
          __preparedStmtOfDeleteStatsForBook.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBookStats(final long bookId, final long totalTime, final int totalSessions,
      final long avgDuration, final float avgPagesPerMin, final float avgWordsPerMin,
      final float completion, final Integer daysToComplete, final long longestSession,
      final long shortestSession, final long lastUpdated,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBookStats.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, totalTime);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, totalSessions);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, avgDuration);
        _argIndex = 4;
        _stmt.bindDouble(_argIndex, avgPagesPerMin);
        _argIndex = 5;
        _stmt.bindDouble(_argIndex, avgWordsPerMin);
        _argIndex = 6;
        _stmt.bindDouble(_argIndex, completion);
        _argIndex = 7;
        if (daysToComplete == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, daysToComplete);
        }
        _argIndex = 8;
        _stmt.bindLong(_argIndex, longestSession);
        _argIndex = 9;
        _stmt.bindLong(_argIndex, shortestSession);
        _argIndex = 10;
        _stmt.bindLong(_argIndex, lastUpdated);
        _argIndex = 11;
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
          __preparedStmtOfUpdateBookStats.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStreak(final long bookId, final int currentStreak,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStreak.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, currentStreak);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, currentStreak);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, currentStreak);
        _argIndex = 4;
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
          __preparedStmtOfUpdateStreak.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ReadingStats> getStatsForBook(final long bookId) {
    final String _sql = "SELECT * FROM reading_stats WHERE bookId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reading_stats"}, new Callable<ReadingStats>() {
      @Override
      @Nullable
      public ReadingStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfTotalReadingTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReadingTimeMillis");
          final int _cursorIndexOfTotalSessions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSessions");
          final int _cursorIndexOfAverageSessionDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSessionDurationMillis");
          final int _cursorIndexOfAveragePagesPerMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "averagePagesPerMinute");
          final int _cursorIndexOfAverageWordsPerMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "averageWordsPerMinute");
          final int _cursorIndexOfCompletionPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "completionPercentage");
          final int _cursorIndexOfDaysToComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "daysToComplete");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfLongestSessionMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "longestSessionMillis");
          final int _cursorIndexOfShortestSessionMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "shortestSessionMillis");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final ReadingStats _result;
          if (_cursor.moveToFirst()) {
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpTotalReadingTimeMillis;
            _tmpTotalReadingTimeMillis = _cursor.getLong(_cursorIndexOfTotalReadingTimeMillis);
            final int _tmpTotalSessions;
            _tmpTotalSessions = _cursor.getInt(_cursorIndexOfTotalSessions);
            final long _tmpAverageSessionDurationMillis;
            _tmpAverageSessionDurationMillis = _cursor.getLong(_cursorIndexOfAverageSessionDurationMillis);
            final float _tmpAveragePagesPerMinute;
            _tmpAveragePagesPerMinute = _cursor.getFloat(_cursorIndexOfAveragePagesPerMinute);
            final float _tmpAverageWordsPerMinute;
            _tmpAverageWordsPerMinute = _cursor.getFloat(_cursorIndexOfAverageWordsPerMinute);
            final float _tmpCompletionPercentage;
            _tmpCompletionPercentage = _cursor.getFloat(_cursorIndexOfCompletionPercentage);
            final Integer _tmpDaysToComplete;
            if (_cursor.isNull(_cursorIndexOfDaysToComplete)) {
              _tmpDaysToComplete = null;
            } else {
              _tmpDaysToComplete = _cursor.getInt(_cursorIndexOfDaysToComplete);
            }
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final long _tmpLongestSessionMillis;
            _tmpLongestSessionMillis = _cursor.getLong(_cursorIndexOfLongestSessionMillis);
            final long _tmpShortestSessionMillis;
            _tmpShortestSessionMillis = _cursor.getLong(_cursorIndexOfShortestSessionMillis);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            _result = new ReadingStats(_tmpBookId,_tmpTotalReadingTimeMillis,_tmpTotalSessions,_tmpAverageSessionDurationMillis,_tmpAveragePagesPerMinute,_tmpAverageWordsPerMinute,_tmpCompletionPercentage,_tmpDaysToComplete,_tmpLastUpdated,_tmpLongestSessionMillis,_tmpShortestSessionMillis,_tmpCurrentStreak,_tmpLongestStreak);
          } else {
            _result = null;
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
  public Object getStatsForBookSync(final long bookId,
      final Continuation<? super ReadingStats> $completion) {
    final String _sql = "SELECT * FROM reading_stats WHERE bookId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReadingStats>() {
      @Override
      @Nullable
      public ReadingStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfTotalReadingTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReadingTimeMillis");
          final int _cursorIndexOfTotalSessions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSessions");
          final int _cursorIndexOfAverageSessionDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSessionDurationMillis");
          final int _cursorIndexOfAveragePagesPerMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "averagePagesPerMinute");
          final int _cursorIndexOfAverageWordsPerMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "averageWordsPerMinute");
          final int _cursorIndexOfCompletionPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "completionPercentage");
          final int _cursorIndexOfDaysToComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "daysToComplete");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfLongestSessionMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "longestSessionMillis");
          final int _cursorIndexOfShortestSessionMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "shortestSessionMillis");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final ReadingStats _result;
          if (_cursor.moveToFirst()) {
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpTotalReadingTimeMillis;
            _tmpTotalReadingTimeMillis = _cursor.getLong(_cursorIndexOfTotalReadingTimeMillis);
            final int _tmpTotalSessions;
            _tmpTotalSessions = _cursor.getInt(_cursorIndexOfTotalSessions);
            final long _tmpAverageSessionDurationMillis;
            _tmpAverageSessionDurationMillis = _cursor.getLong(_cursorIndexOfAverageSessionDurationMillis);
            final float _tmpAveragePagesPerMinute;
            _tmpAveragePagesPerMinute = _cursor.getFloat(_cursorIndexOfAveragePagesPerMinute);
            final float _tmpAverageWordsPerMinute;
            _tmpAverageWordsPerMinute = _cursor.getFloat(_cursorIndexOfAverageWordsPerMinute);
            final float _tmpCompletionPercentage;
            _tmpCompletionPercentage = _cursor.getFloat(_cursorIndexOfCompletionPercentage);
            final Integer _tmpDaysToComplete;
            if (_cursor.isNull(_cursorIndexOfDaysToComplete)) {
              _tmpDaysToComplete = null;
            } else {
              _tmpDaysToComplete = _cursor.getInt(_cursorIndexOfDaysToComplete);
            }
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final long _tmpLongestSessionMillis;
            _tmpLongestSessionMillis = _cursor.getLong(_cursorIndexOfLongestSessionMillis);
            final long _tmpShortestSessionMillis;
            _tmpShortestSessionMillis = _cursor.getLong(_cursorIndexOfShortestSessionMillis);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            _result = new ReadingStats(_tmpBookId,_tmpTotalReadingTimeMillis,_tmpTotalSessions,_tmpAverageSessionDurationMillis,_tmpAveragePagesPerMinute,_tmpAverageWordsPerMinute,_tmpCompletionPercentage,_tmpDaysToComplete,_tmpLastUpdated,_tmpLongestSessionMillis,_tmpShortestSessionMillis,_tmpCurrentStreak,_tmpLongestStreak);
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
  public Flow<List<ReadingStats>> getAllStats() {
    final String _sql = "SELECT * FROM reading_stats";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reading_stats"}, new Callable<List<ReadingStats>>() {
      @Override
      @NonNull
      public List<ReadingStats> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfTotalReadingTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReadingTimeMillis");
          final int _cursorIndexOfTotalSessions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSessions");
          final int _cursorIndexOfAverageSessionDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSessionDurationMillis");
          final int _cursorIndexOfAveragePagesPerMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "averagePagesPerMinute");
          final int _cursorIndexOfAverageWordsPerMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "averageWordsPerMinute");
          final int _cursorIndexOfCompletionPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "completionPercentage");
          final int _cursorIndexOfDaysToComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "daysToComplete");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfLongestSessionMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "longestSessionMillis");
          final int _cursorIndexOfShortestSessionMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "shortestSessionMillis");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final List<ReadingStats> _result = new ArrayList<ReadingStats>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReadingStats _item;
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpTotalReadingTimeMillis;
            _tmpTotalReadingTimeMillis = _cursor.getLong(_cursorIndexOfTotalReadingTimeMillis);
            final int _tmpTotalSessions;
            _tmpTotalSessions = _cursor.getInt(_cursorIndexOfTotalSessions);
            final long _tmpAverageSessionDurationMillis;
            _tmpAverageSessionDurationMillis = _cursor.getLong(_cursorIndexOfAverageSessionDurationMillis);
            final float _tmpAveragePagesPerMinute;
            _tmpAveragePagesPerMinute = _cursor.getFloat(_cursorIndexOfAveragePagesPerMinute);
            final float _tmpAverageWordsPerMinute;
            _tmpAverageWordsPerMinute = _cursor.getFloat(_cursorIndexOfAverageWordsPerMinute);
            final float _tmpCompletionPercentage;
            _tmpCompletionPercentage = _cursor.getFloat(_cursorIndexOfCompletionPercentage);
            final Integer _tmpDaysToComplete;
            if (_cursor.isNull(_cursorIndexOfDaysToComplete)) {
              _tmpDaysToComplete = null;
            } else {
              _tmpDaysToComplete = _cursor.getInt(_cursorIndexOfDaysToComplete);
            }
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final long _tmpLongestSessionMillis;
            _tmpLongestSessionMillis = _cursor.getLong(_cursorIndexOfLongestSessionMillis);
            final long _tmpShortestSessionMillis;
            _tmpShortestSessionMillis = _cursor.getLong(_cursorIndexOfShortestSessionMillis);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            _item = new ReadingStats(_tmpBookId,_tmpTotalReadingTimeMillis,_tmpTotalSessions,_tmpAverageSessionDurationMillis,_tmpAveragePagesPerMinute,_tmpAverageWordsPerMinute,_tmpCompletionPercentage,_tmpDaysToComplete,_tmpLastUpdated,_tmpLongestSessionMillis,_tmpShortestSessionMillis,_tmpCurrentStreak,_tmpLongestStreak);
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
  public Object getFastestReadBook(final Continuation<? super ReadingStats> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM reading_stats \n"
            + "        WHERE completionPercentage >= 95 AND totalReadingTimeMillis > 0\n"
            + "        ORDER BY totalReadingTimeMillis ASC \n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReadingStats>() {
      @Override
      @Nullable
      public ReadingStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfTotalReadingTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReadingTimeMillis");
          final int _cursorIndexOfTotalSessions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSessions");
          final int _cursorIndexOfAverageSessionDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSessionDurationMillis");
          final int _cursorIndexOfAveragePagesPerMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "averagePagesPerMinute");
          final int _cursorIndexOfAverageWordsPerMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "averageWordsPerMinute");
          final int _cursorIndexOfCompletionPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "completionPercentage");
          final int _cursorIndexOfDaysToComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "daysToComplete");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfLongestSessionMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "longestSessionMillis");
          final int _cursorIndexOfShortestSessionMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "shortestSessionMillis");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final ReadingStats _result;
          if (_cursor.moveToFirst()) {
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final long _tmpTotalReadingTimeMillis;
            _tmpTotalReadingTimeMillis = _cursor.getLong(_cursorIndexOfTotalReadingTimeMillis);
            final int _tmpTotalSessions;
            _tmpTotalSessions = _cursor.getInt(_cursorIndexOfTotalSessions);
            final long _tmpAverageSessionDurationMillis;
            _tmpAverageSessionDurationMillis = _cursor.getLong(_cursorIndexOfAverageSessionDurationMillis);
            final float _tmpAveragePagesPerMinute;
            _tmpAveragePagesPerMinute = _cursor.getFloat(_cursorIndexOfAveragePagesPerMinute);
            final float _tmpAverageWordsPerMinute;
            _tmpAverageWordsPerMinute = _cursor.getFloat(_cursorIndexOfAverageWordsPerMinute);
            final float _tmpCompletionPercentage;
            _tmpCompletionPercentage = _cursor.getFloat(_cursorIndexOfCompletionPercentage);
            final Integer _tmpDaysToComplete;
            if (_cursor.isNull(_cursorIndexOfDaysToComplete)) {
              _tmpDaysToComplete = null;
            } else {
              _tmpDaysToComplete = _cursor.getInt(_cursorIndexOfDaysToComplete);
            }
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final long _tmpLongestSessionMillis;
            _tmpLongestSessionMillis = _cursor.getLong(_cursorIndexOfLongestSessionMillis);
            final long _tmpShortestSessionMillis;
            _tmpShortestSessionMillis = _cursor.getLong(_cursorIndexOfShortestSessionMillis);
            final int _tmpCurrentStreak;
            _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            _result = new ReadingStats(_tmpBookId,_tmpTotalReadingTimeMillis,_tmpTotalSessions,_tmpAverageSessionDurationMillis,_tmpAveragePagesPerMinute,_tmpAverageWordsPerMinute,_tmpCompletionPercentage,_tmpDaysToComplete,_tmpLastUpdated,_tmpLongestSessionMillis,_tmpShortestSessionMillis,_tmpCurrentStreak,_tmpLongestStreak);
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
  public Object getAverageDailyReadingTime(final Continuation<? super Long> $completion) {
    final String _sql = "\n"
            + "        SELECT AVG(totalReadingTimeMillis / NULLIF(daysToComplete, 0)) \n"
            + "        FROM reading_stats \n"
            + "        WHERE daysToComplete > 0\n"
            + "    ";
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
