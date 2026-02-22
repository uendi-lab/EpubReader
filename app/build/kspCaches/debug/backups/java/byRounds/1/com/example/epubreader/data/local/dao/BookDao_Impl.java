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
import com.example.epubreader.data.local.entity.Book;
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
public final class BookDao_Impl implements BookDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Book> __insertionAdapterOfBook;

  private final EntityDeletionOrUpdateAdapter<Book> __deletionAdapterOfBook;

  private final EntityDeletionOrUpdateAdapter<Book> __updateAdapterOfBook;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBookById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateReadingProgress;

  private final SharedSQLiteStatement __preparedStmtOfSetStartDateIfNull;

  private final SharedSQLiteStatement __preparedStmtOfUpdateReadingSettings;

  private final SharedSQLiteStatement __preparedStmtOfToggleFavorite;

  public BookDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBook = new EntityInsertionAdapter<Book>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `books` (`id`,`title`,`author`,`filePath`,`coverImagePath`,`totalPages`,`totalCharacters`,`addedDate`,`startDate`,`endDate`,`currentPosition`,`currentChapterIndex`,`currentChapterProgress`,`completionPercentage`,`isCompleted`,`isFavorite`,`lastReadDate`,`fontSize`,`isDarkMode`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Book entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getAuthor());
        statement.bindString(4, entity.getFilePath());
        if (entity.getCoverImagePath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCoverImagePath());
        }
        statement.bindLong(6, entity.getTotalPages());
        statement.bindLong(7, entity.getTotalCharacters());
        statement.bindLong(8, entity.getAddedDate());
        if (entity.getStartDate() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getEndDate());
        }
        statement.bindLong(11, entity.getCurrentPosition());
        statement.bindLong(12, entity.getCurrentChapterIndex());
        statement.bindDouble(13, entity.getCurrentChapterProgress());
        statement.bindDouble(14, entity.getCompletionPercentage());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(15, _tmp);
        final int _tmp_1 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(16, _tmp_1);
        if (entity.getLastReadDate() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getLastReadDate());
        }
        statement.bindDouble(18, entity.getFontSize());
        final int _tmp_2 = entity.isDarkMode() ? 1 : 0;
        statement.bindLong(19, _tmp_2);
      }
    };
    this.__deletionAdapterOfBook = new EntityDeletionOrUpdateAdapter<Book>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `books` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Book entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfBook = new EntityDeletionOrUpdateAdapter<Book>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `books` SET `id` = ?,`title` = ?,`author` = ?,`filePath` = ?,`coverImagePath` = ?,`totalPages` = ?,`totalCharacters` = ?,`addedDate` = ?,`startDate` = ?,`endDate` = ?,`currentPosition` = ?,`currentChapterIndex` = ?,`currentChapterProgress` = ?,`completionPercentage` = ?,`isCompleted` = ?,`isFavorite` = ?,`lastReadDate` = ?,`fontSize` = ?,`isDarkMode` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Book entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getAuthor());
        statement.bindString(4, entity.getFilePath());
        if (entity.getCoverImagePath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCoverImagePath());
        }
        statement.bindLong(6, entity.getTotalPages());
        statement.bindLong(7, entity.getTotalCharacters());
        statement.bindLong(8, entity.getAddedDate());
        if (entity.getStartDate() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getEndDate());
        }
        statement.bindLong(11, entity.getCurrentPosition());
        statement.bindLong(12, entity.getCurrentChapterIndex());
        statement.bindDouble(13, entity.getCurrentChapterProgress());
        statement.bindDouble(14, entity.getCompletionPercentage());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(15, _tmp);
        final int _tmp_1 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(16, _tmp_1);
        if (entity.getLastReadDate() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getLastReadDate());
        }
        statement.bindDouble(18, entity.getFontSize());
        final int _tmp_2 = entity.isDarkMode() ? 1 : 0;
        statement.bindLong(19, _tmp_2);
        statement.bindLong(20, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteBookById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM books WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateReadingProgress = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE books \n"
                + "        SET currentPosition = ?,\n"
                + "            currentChapterIndex = ?,\n"
                + "            currentChapterProgress = ?,\n"
                + "            completionPercentage = ?,\n"
                + "            lastReadDate = ?,\n"
                + "            isCompleted = ?,\n"
                + "            endDate = ?\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfSetStartDateIfNull = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET startDate = ? WHERE id = ? AND startDate IS NULL";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateReadingSettings = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET fontSize = ?, isDarkMode = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfToggleFavorite = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET isFavorite = NOT isFavorite WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertBook(final Book book, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBook.insertAndReturnId(book);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBook(final Book book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBook.handle(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBook(final Book book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBook.handle(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBookById(final long bookId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBookById.acquire();
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
          __preparedStmtOfDeleteBookById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReadingProgress(final long bookId, final int position, final int chapterIndex,
      final float chapterProgress, final float completionPercentage, final long lastReadDate,
      final boolean isCompleted, final Long endDate, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateReadingProgress.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, position);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, chapterIndex);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, chapterProgress);
        _argIndex = 4;
        _stmt.bindDouble(_argIndex, completionPercentage);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, lastReadDate);
        _argIndex = 6;
        final int _tmp = isCompleted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 7;
        if (endDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, endDate);
        }
        _argIndex = 8;
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
          __preparedStmtOfUpdateReadingProgress.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setStartDateIfNull(final long bookId, final long startDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetStartDateIfNull.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, startDate);
        _argIndex = 2;
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
          __preparedStmtOfSetStartDateIfNull.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReadingSettings(final long bookId, final float fontSize,
      final boolean isDarkMode, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateReadingSettings.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, fontSize);
        _argIndex = 2;
        final int _tmp = isDarkMode ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 3;
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
          __preparedStmtOfUpdateReadingSettings.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object toggleFavorite(final long bookId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfToggleFavorite.acquire();
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
          __preparedStmtOfToggleFavorite.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Book>> getAllBooks() {
    final String _sql = "SELECT * FROM books ORDER BY lastReadDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfCoverImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImagePath");
          final int _cursorIndexOfTotalPages = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPages");
          final int _cursorIndexOfTotalCharacters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCharacters");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfCurrentPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPosition");
          final int _cursorIndexOfCurrentChapterIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterIndex");
          final int _cursorIndexOfCurrentChapterProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterProgress");
          final int _cursorIndexOfCompletionPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "completionPercentage");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastReadDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadDate");
          final int _cursorIndexOfFontSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fontSize");
          final int _cursorIndexOfIsDarkMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isDarkMode");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final String _tmpCoverImagePath;
            if (_cursor.isNull(_cursorIndexOfCoverImagePath)) {
              _tmpCoverImagePath = null;
            } else {
              _tmpCoverImagePath = _cursor.getString(_cursorIndexOfCoverImagePath);
            }
            final int _tmpTotalPages;
            _tmpTotalPages = _cursor.getInt(_cursorIndexOfTotalPages);
            final long _tmpTotalCharacters;
            _tmpTotalCharacters = _cursor.getLong(_cursorIndexOfTotalCharacters);
            final long _tmpAddedDate;
            _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            final Long _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final int _tmpCurrentPosition;
            _tmpCurrentPosition = _cursor.getInt(_cursorIndexOfCurrentPosition);
            final int _tmpCurrentChapterIndex;
            _tmpCurrentChapterIndex = _cursor.getInt(_cursorIndexOfCurrentChapterIndex);
            final float _tmpCurrentChapterProgress;
            _tmpCurrentChapterProgress = _cursor.getFloat(_cursorIndexOfCurrentChapterProgress);
            final float _tmpCompletionPercentage;
            _tmpCompletionPercentage = _cursor.getFloat(_cursorIndexOfCompletionPercentage);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final Long _tmpLastReadDate;
            if (_cursor.isNull(_cursorIndexOfLastReadDate)) {
              _tmpLastReadDate = null;
            } else {
              _tmpLastReadDate = _cursor.getLong(_cursorIndexOfLastReadDate);
            }
            final float _tmpFontSize;
            _tmpFontSize = _cursor.getFloat(_cursorIndexOfFontSize);
            final boolean _tmpIsDarkMode;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDarkMode);
            _tmpIsDarkMode = _tmp_2 != 0;
            _item = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpFilePath,_tmpCoverImagePath,_tmpTotalPages,_tmpTotalCharacters,_tmpAddedDate,_tmpStartDate,_tmpEndDate,_tmpCurrentPosition,_tmpCurrentChapterIndex,_tmpCurrentChapterProgress,_tmpCompletionPercentage,_tmpIsCompleted,_tmpIsFavorite,_tmpLastReadDate,_tmpFontSize,_tmpIsDarkMode);
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
  public Flow<Book> getBookById(final long bookId) {
    final String _sql = "SELECT * FROM books WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<Book>() {
      @Override
      @Nullable
      public Book call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfCoverImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImagePath");
          final int _cursorIndexOfTotalPages = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPages");
          final int _cursorIndexOfTotalCharacters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCharacters");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfCurrentPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPosition");
          final int _cursorIndexOfCurrentChapterIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterIndex");
          final int _cursorIndexOfCurrentChapterProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterProgress");
          final int _cursorIndexOfCompletionPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "completionPercentage");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastReadDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadDate");
          final int _cursorIndexOfFontSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fontSize");
          final int _cursorIndexOfIsDarkMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isDarkMode");
          final Book _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final String _tmpCoverImagePath;
            if (_cursor.isNull(_cursorIndexOfCoverImagePath)) {
              _tmpCoverImagePath = null;
            } else {
              _tmpCoverImagePath = _cursor.getString(_cursorIndexOfCoverImagePath);
            }
            final int _tmpTotalPages;
            _tmpTotalPages = _cursor.getInt(_cursorIndexOfTotalPages);
            final long _tmpTotalCharacters;
            _tmpTotalCharacters = _cursor.getLong(_cursorIndexOfTotalCharacters);
            final long _tmpAddedDate;
            _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            final Long _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final int _tmpCurrentPosition;
            _tmpCurrentPosition = _cursor.getInt(_cursorIndexOfCurrentPosition);
            final int _tmpCurrentChapterIndex;
            _tmpCurrentChapterIndex = _cursor.getInt(_cursorIndexOfCurrentChapterIndex);
            final float _tmpCurrentChapterProgress;
            _tmpCurrentChapterProgress = _cursor.getFloat(_cursorIndexOfCurrentChapterProgress);
            final float _tmpCompletionPercentage;
            _tmpCompletionPercentage = _cursor.getFloat(_cursorIndexOfCompletionPercentage);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final Long _tmpLastReadDate;
            if (_cursor.isNull(_cursorIndexOfLastReadDate)) {
              _tmpLastReadDate = null;
            } else {
              _tmpLastReadDate = _cursor.getLong(_cursorIndexOfLastReadDate);
            }
            final float _tmpFontSize;
            _tmpFontSize = _cursor.getFloat(_cursorIndexOfFontSize);
            final boolean _tmpIsDarkMode;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDarkMode);
            _tmpIsDarkMode = _tmp_2 != 0;
            _result = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpFilePath,_tmpCoverImagePath,_tmpTotalPages,_tmpTotalCharacters,_tmpAddedDate,_tmpStartDate,_tmpEndDate,_tmpCurrentPosition,_tmpCurrentChapterIndex,_tmpCurrentChapterProgress,_tmpCompletionPercentage,_tmpIsCompleted,_tmpIsFavorite,_tmpLastReadDate,_tmpFontSize,_tmpIsDarkMode);
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
  public Object getBookByIdSync(final long bookId, final Continuation<? super Book> $completion) {
    final String _sql = "SELECT * FROM books WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Book>() {
      @Override
      @Nullable
      public Book call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfCoverImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImagePath");
          final int _cursorIndexOfTotalPages = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPages");
          final int _cursorIndexOfTotalCharacters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCharacters");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfCurrentPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPosition");
          final int _cursorIndexOfCurrentChapterIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterIndex");
          final int _cursorIndexOfCurrentChapterProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterProgress");
          final int _cursorIndexOfCompletionPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "completionPercentage");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastReadDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadDate");
          final int _cursorIndexOfFontSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fontSize");
          final int _cursorIndexOfIsDarkMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isDarkMode");
          final Book _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final String _tmpCoverImagePath;
            if (_cursor.isNull(_cursorIndexOfCoverImagePath)) {
              _tmpCoverImagePath = null;
            } else {
              _tmpCoverImagePath = _cursor.getString(_cursorIndexOfCoverImagePath);
            }
            final int _tmpTotalPages;
            _tmpTotalPages = _cursor.getInt(_cursorIndexOfTotalPages);
            final long _tmpTotalCharacters;
            _tmpTotalCharacters = _cursor.getLong(_cursorIndexOfTotalCharacters);
            final long _tmpAddedDate;
            _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            final Long _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final int _tmpCurrentPosition;
            _tmpCurrentPosition = _cursor.getInt(_cursorIndexOfCurrentPosition);
            final int _tmpCurrentChapterIndex;
            _tmpCurrentChapterIndex = _cursor.getInt(_cursorIndexOfCurrentChapterIndex);
            final float _tmpCurrentChapterProgress;
            _tmpCurrentChapterProgress = _cursor.getFloat(_cursorIndexOfCurrentChapterProgress);
            final float _tmpCompletionPercentage;
            _tmpCompletionPercentage = _cursor.getFloat(_cursorIndexOfCompletionPercentage);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final Long _tmpLastReadDate;
            if (_cursor.isNull(_cursorIndexOfLastReadDate)) {
              _tmpLastReadDate = null;
            } else {
              _tmpLastReadDate = _cursor.getLong(_cursorIndexOfLastReadDate);
            }
            final float _tmpFontSize;
            _tmpFontSize = _cursor.getFloat(_cursorIndexOfFontSize);
            final boolean _tmpIsDarkMode;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDarkMode);
            _tmpIsDarkMode = _tmp_2 != 0;
            _result = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpFilePath,_tmpCoverImagePath,_tmpTotalPages,_tmpTotalCharacters,_tmpAddedDate,_tmpStartDate,_tmpEndDate,_tmpCurrentPosition,_tmpCurrentChapterIndex,_tmpCurrentChapterProgress,_tmpCompletionPercentage,_tmpIsCompleted,_tmpIsFavorite,_tmpLastReadDate,_tmpFontSize,_tmpIsDarkMode);
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
  public Flow<List<Book>> getInProgressBooks() {
    final String _sql = "SELECT * FROM books WHERE isCompleted = 0 ORDER BY lastReadDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfCoverImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImagePath");
          final int _cursorIndexOfTotalPages = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPages");
          final int _cursorIndexOfTotalCharacters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCharacters");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfCurrentPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPosition");
          final int _cursorIndexOfCurrentChapterIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterIndex");
          final int _cursorIndexOfCurrentChapterProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterProgress");
          final int _cursorIndexOfCompletionPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "completionPercentage");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastReadDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadDate");
          final int _cursorIndexOfFontSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fontSize");
          final int _cursorIndexOfIsDarkMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isDarkMode");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final String _tmpCoverImagePath;
            if (_cursor.isNull(_cursorIndexOfCoverImagePath)) {
              _tmpCoverImagePath = null;
            } else {
              _tmpCoverImagePath = _cursor.getString(_cursorIndexOfCoverImagePath);
            }
            final int _tmpTotalPages;
            _tmpTotalPages = _cursor.getInt(_cursorIndexOfTotalPages);
            final long _tmpTotalCharacters;
            _tmpTotalCharacters = _cursor.getLong(_cursorIndexOfTotalCharacters);
            final long _tmpAddedDate;
            _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            final Long _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final int _tmpCurrentPosition;
            _tmpCurrentPosition = _cursor.getInt(_cursorIndexOfCurrentPosition);
            final int _tmpCurrentChapterIndex;
            _tmpCurrentChapterIndex = _cursor.getInt(_cursorIndexOfCurrentChapterIndex);
            final float _tmpCurrentChapterProgress;
            _tmpCurrentChapterProgress = _cursor.getFloat(_cursorIndexOfCurrentChapterProgress);
            final float _tmpCompletionPercentage;
            _tmpCompletionPercentage = _cursor.getFloat(_cursorIndexOfCompletionPercentage);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final Long _tmpLastReadDate;
            if (_cursor.isNull(_cursorIndexOfLastReadDate)) {
              _tmpLastReadDate = null;
            } else {
              _tmpLastReadDate = _cursor.getLong(_cursorIndexOfLastReadDate);
            }
            final float _tmpFontSize;
            _tmpFontSize = _cursor.getFloat(_cursorIndexOfFontSize);
            final boolean _tmpIsDarkMode;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDarkMode);
            _tmpIsDarkMode = _tmp_2 != 0;
            _item = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpFilePath,_tmpCoverImagePath,_tmpTotalPages,_tmpTotalCharacters,_tmpAddedDate,_tmpStartDate,_tmpEndDate,_tmpCurrentPosition,_tmpCurrentChapterIndex,_tmpCurrentChapterProgress,_tmpCompletionPercentage,_tmpIsCompleted,_tmpIsFavorite,_tmpLastReadDate,_tmpFontSize,_tmpIsDarkMode);
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
  public Flow<List<Book>> getCompletedBooks() {
    final String _sql = "SELECT * FROM books WHERE isCompleted = 1 ORDER BY endDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfCoverImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImagePath");
          final int _cursorIndexOfTotalPages = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPages");
          final int _cursorIndexOfTotalCharacters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCharacters");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfCurrentPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPosition");
          final int _cursorIndexOfCurrentChapterIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterIndex");
          final int _cursorIndexOfCurrentChapterProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterProgress");
          final int _cursorIndexOfCompletionPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "completionPercentage");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastReadDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadDate");
          final int _cursorIndexOfFontSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fontSize");
          final int _cursorIndexOfIsDarkMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isDarkMode");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final String _tmpCoverImagePath;
            if (_cursor.isNull(_cursorIndexOfCoverImagePath)) {
              _tmpCoverImagePath = null;
            } else {
              _tmpCoverImagePath = _cursor.getString(_cursorIndexOfCoverImagePath);
            }
            final int _tmpTotalPages;
            _tmpTotalPages = _cursor.getInt(_cursorIndexOfTotalPages);
            final long _tmpTotalCharacters;
            _tmpTotalCharacters = _cursor.getLong(_cursorIndexOfTotalCharacters);
            final long _tmpAddedDate;
            _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            final Long _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final int _tmpCurrentPosition;
            _tmpCurrentPosition = _cursor.getInt(_cursorIndexOfCurrentPosition);
            final int _tmpCurrentChapterIndex;
            _tmpCurrentChapterIndex = _cursor.getInt(_cursorIndexOfCurrentChapterIndex);
            final float _tmpCurrentChapterProgress;
            _tmpCurrentChapterProgress = _cursor.getFloat(_cursorIndexOfCurrentChapterProgress);
            final float _tmpCompletionPercentage;
            _tmpCompletionPercentage = _cursor.getFloat(_cursorIndexOfCompletionPercentage);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final Long _tmpLastReadDate;
            if (_cursor.isNull(_cursorIndexOfLastReadDate)) {
              _tmpLastReadDate = null;
            } else {
              _tmpLastReadDate = _cursor.getLong(_cursorIndexOfLastReadDate);
            }
            final float _tmpFontSize;
            _tmpFontSize = _cursor.getFloat(_cursorIndexOfFontSize);
            final boolean _tmpIsDarkMode;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDarkMode);
            _tmpIsDarkMode = _tmp_2 != 0;
            _item = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpFilePath,_tmpCoverImagePath,_tmpTotalPages,_tmpTotalCharacters,_tmpAddedDate,_tmpStartDate,_tmpEndDate,_tmpCurrentPosition,_tmpCurrentChapterIndex,_tmpCurrentChapterProgress,_tmpCompletionPercentage,_tmpIsCompleted,_tmpIsFavorite,_tmpLastReadDate,_tmpFontSize,_tmpIsDarkMode);
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
  public Flow<List<Book>> getFavoriteBooks() {
    final String _sql = "SELECT * FROM books WHERE isFavorite = 1 ORDER BY lastReadDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfCoverImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "coverImagePath");
          final int _cursorIndexOfTotalPages = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPages");
          final int _cursorIndexOfTotalCharacters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCharacters");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfCurrentPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPosition");
          final int _cursorIndexOfCurrentChapterIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterIndex");
          final int _cursorIndexOfCurrentChapterProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "currentChapterProgress");
          final int _cursorIndexOfCompletionPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "completionPercentage");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastReadDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadDate");
          final int _cursorIndexOfFontSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fontSize");
          final int _cursorIndexOfIsDarkMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isDarkMode");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final String _tmpCoverImagePath;
            if (_cursor.isNull(_cursorIndexOfCoverImagePath)) {
              _tmpCoverImagePath = null;
            } else {
              _tmpCoverImagePath = _cursor.getString(_cursorIndexOfCoverImagePath);
            }
            final int _tmpTotalPages;
            _tmpTotalPages = _cursor.getInt(_cursorIndexOfTotalPages);
            final long _tmpTotalCharacters;
            _tmpTotalCharacters = _cursor.getLong(_cursorIndexOfTotalCharacters);
            final long _tmpAddedDate;
            _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            final Long _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final int _tmpCurrentPosition;
            _tmpCurrentPosition = _cursor.getInt(_cursorIndexOfCurrentPosition);
            final int _tmpCurrentChapterIndex;
            _tmpCurrentChapterIndex = _cursor.getInt(_cursorIndexOfCurrentChapterIndex);
            final float _tmpCurrentChapterProgress;
            _tmpCurrentChapterProgress = _cursor.getFloat(_cursorIndexOfCurrentChapterProgress);
            final float _tmpCompletionPercentage;
            _tmpCompletionPercentage = _cursor.getFloat(_cursorIndexOfCompletionPercentage);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final Long _tmpLastReadDate;
            if (_cursor.isNull(_cursorIndexOfLastReadDate)) {
              _tmpLastReadDate = null;
            } else {
              _tmpLastReadDate = _cursor.getLong(_cursorIndexOfLastReadDate);
            }
            final float _tmpFontSize;
            _tmpFontSize = _cursor.getFloat(_cursorIndexOfFontSize);
            final boolean _tmpIsDarkMode;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDarkMode);
            _tmpIsDarkMode = _tmp_2 != 0;
            _item = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpFilePath,_tmpCoverImagePath,_tmpTotalPages,_tmpTotalCharacters,_tmpAddedDate,_tmpStartDate,_tmpEndDate,_tmpCurrentPosition,_tmpCurrentChapterIndex,_tmpCurrentChapterProgress,_tmpCompletionPercentage,_tmpIsCompleted,_tmpIsFavorite,_tmpLastReadDate,_tmpFontSize,_tmpIsDarkMode);
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
  public Flow<Integer> getTotalBooksCount() {
    final String _sql = "SELECT COUNT(*) FROM books";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<Integer>() {
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

  @Override
  public Flow<Integer> getCompletedBooksCount() {
    final String _sql = "SELECT COUNT(*) FROM books WHERE isCompleted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<Integer>() {
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
