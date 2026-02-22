package com.example.epubreader.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.epubreader.data.local.entity.Highlight;
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
public final class HighlightDao_Impl implements HighlightDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Highlight> __insertionAdapterOfHighlight;

  private final EntityDeletionOrUpdateAdapter<Highlight> __deletionAdapterOfHighlight;

  private final EntityDeletionOrUpdateAdapter<Highlight> __updateAdapterOfHighlight;

  private final SharedSQLiteStatement __preparedStmtOfDeleteHighlightsForBook;

  public HighlightDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHighlight = new EntityInsertionAdapter<Highlight>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `highlights` (`id`,`bookId`,`chapterIndex`,`startPosition`,`endPosition`,`highlightedText`,`color`,`note`,`createdDate`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Highlight entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBookId());
        statement.bindLong(3, entity.getChapterIndex());
        statement.bindLong(4, entity.getStartPosition());
        statement.bindLong(5, entity.getEndPosition());
        statement.bindString(6, entity.getHighlightedText());
        statement.bindString(7, entity.getColor());
        if (entity.getNote() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNote());
        }
        statement.bindLong(9, entity.getCreatedDate());
      }
    };
    this.__deletionAdapterOfHighlight = new EntityDeletionOrUpdateAdapter<Highlight>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `highlights` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Highlight entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfHighlight = new EntityDeletionOrUpdateAdapter<Highlight>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `highlights` SET `id` = ?,`bookId` = ?,`chapterIndex` = ?,`startPosition` = ?,`endPosition` = ?,`highlightedText` = ?,`color` = ?,`note` = ?,`createdDate` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Highlight entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBookId());
        statement.bindLong(3, entity.getChapterIndex());
        statement.bindLong(4, entity.getStartPosition());
        statement.bindLong(5, entity.getEndPosition());
        statement.bindString(6, entity.getHighlightedText());
        statement.bindString(7, entity.getColor());
        if (entity.getNote() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNote());
        }
        statement.bindLong(9, entity.getCreatedDate());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteHighlightsForBook = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM highlights WHERE bookId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertHighlight(final Highlight highlight,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHighlight.insertAndReturnId(highlight);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHighlight(final Highlight highlight,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHighlight.handle(highlight);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateHighlight(final Highlight highlight,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfHighlight.handle(highlight);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHighlightsForBook(final long bookId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteHighlightsForBook.acquire();
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
          __preparedStmtOfDeleteHighlightsForBook.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Highlight>> getHighlightsForBook(final long bookId) {
    final String _sql = "SELECT * FROM highlights WHERE bookId = ? ORDER BY chapterIndex, startPosition";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"highlights"}, new Callable<List<Highlight>>() {
      @Override
      @NonNull
      public List<Highlight> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfChapterIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterIndex");
          final int _cursorIndexOfStartPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "startPosition");
          final int _cursorIndexOfEndPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "endPosition");
          final int _cursorIndexOfHighlightedText = CursorUtil.getColumnIndexOrThrow(_cursor, "highlightedText");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final List<Highlight> _result = new ArrayList<Highlight>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Highlight _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final int _tmpChapterIndex;
            _tmpChapterIndex = _cursor.getInt(_cursorIndexOfChapterIndex);
            final int _tmpStartPosition;
            _tmpStartPosition = _cursor.getInt(_cursorIndexOfStartPosition);
            final int _tmpEndPosition;
            _tmpEndPosition = _cursor.getInt(_cursorIndexOfEndPosition);
            final String _tmpHighlightedText;
            _tmpHighlightedText = _cursor.getString(_cursorIndexOfHighlightedText);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            _item = new Highlight(_tmpId,_tmpBookId,_tmpChapterIndex,_tmpStartPosition,_tmpEndPosition,_tmpHighlightedText,_tmpColor,_tmpNote,_tmpCreatedDate);
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
  public Object getHighlightsForChapter(final long bookId, final int chapterIndex,
      final Continuation<? super List<Highlight>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM highlights \n"
            + "        WHERE bookId = ? AND chapterIndex = ? \n"
            + "        ORDER BY startPosition\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, chapterIndex);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Highlight>>() {
      @Override
      @NonNull
      public List<Highlight> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfChapterIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterIndex");
          final int _cursorIndexOfStartPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "startPosition");
          final int _cursorIndexOfEndPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "endPosition");
          final int _cursorIndexOfHighlightedText = CursorUtil.getColumnIndexOrThrow(_cursor, "highlightedText");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final List<Highlight> _result = new ArrayList<Highlight>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Highlight _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBookId;
            _tmpBookId = _cursor.getLong(_cursorIndexOfBookId);
            final int _tmpChapterIndex;
            _tmpChapterIndex = _cursor.getInt(_cursorIndexOfChapterIndex);
            final int _tmpStartPosition;
            _tmpStartPosition = _cursor.getInt(_cursorIndexOfStartPosition);
            final int _tmpEndPosition;
            _tmpEndPosition = _cursor.getInt(_cursorIndexOfEndPosition);
            final String _tmpHighlightedText;
            _tmpHighlightedText = _cursor.getString(_cursorIndexOfHighlightedText);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            _item = new Highlight(_tmpId,_tmpBookId,_tmpChapterIndex,_tmpStartPosition,_tmpEndPosition,_tmpHighlightedText,_tmpColor,_tmpNote,_tmpCreatedDate);
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
  public Flow<Integer> getHighlightCountForBook(final long bookId) {
    final String _sql = "SELECT COUNT(*) FROM highlights WHERE bookId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"highlights"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
