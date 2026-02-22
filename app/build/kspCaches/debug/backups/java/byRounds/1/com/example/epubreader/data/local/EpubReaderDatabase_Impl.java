package com.example.epubreader.data.local;

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
import com.example.epubreader.data.local.dao.BookDao;
import com.example.epubreader.data.local.dao.BookDao_Impl;
import com.example.epubreader.data.local.dao.BookmarkDao;
import com.example.epubreader.data.local.dao.BookmarkDao_Impl;
import com.example.epubreader.data.local.dao.HighlightDao;
import com.example.epubreader.data.local.dao.HighlightDao_Impl;
import com.example.epubreader.data.local.dao.ReadingSessionDao;
import com.example.epubreader.data.local.dao.ReadingSessionDao_Impl;
import com.example.epubreader.data.local.dao.ReadingStatsDao;
import com.example.epubreader.data.local.dao.ReadingStatsDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class EpubReaderDatabase_Impl extends EpubReaderDatabase {
  private volatile BookDao _bookDao;

  private volatile ReadingSessionDao _readingSessionDao;

  private volatile ReadingStatsDao _readingStatsDao;

  private volatile BookmarkDao _bookmarkDao;

  private volatile HighlightDao _highlightDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `books` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `author` TEXT NOT NULL, `filePath` TEXT NOT NULL, `coverImagePath` TEXT, `totalPages` INTEGER NOT NULL, `totalCharacters` INTEGER NOT NULL, `addedDate` INTEGER NOT NULL, `startDate` INTEGER, `endDate` INTEGER, `currentPosition` INTEGER NOT NULL, `currentChapterIndex` INTEGER NOT NULL, `currentChapterProgress` REAL NOT NULL, `completionPercentage` REAL NOT NULL, `isCompleted` INTEGER NOT NULL, `isFavorite` INTEGER NOT NULL, `lastReadDate` INTEGER, `fontSize` REAL NOT NULL, `isDarkMode` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reading_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bookId` INTEGER NOT NULL, `sessionStart` INTEGER NOT NULL, `sessionEnd` INTEGER, `durationMillis` INTEGER NOT NULL, `pagesRead` INTEGER NOT NULL, `startPosition` INTEGER NOT NULL, `endPosition` INTEGER NOT NULL, `isActive` INTEGER NOT NULL, FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reading_sessions_bookId` ON `reading_sessions` (`bookId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reading_sessions_sessionStart` ON `reading_sessions` (`sessionStart`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reading_stats` (`bookId` INTEGER NOT NULL, `totalReadingTimeMillis` INTEGER NOT NULL, `totalSessions` INTEGER NOT NULL, `averageSessionDurationMillis` INTEGER NOT NULL, `averagePagesPerMinute` REAL NOT NULL, `averageWordsPerMinute` REAL NOT NULL, `completionPercentage` REAL NOT NULL, `daysToComplete` INTEGER, `lastUpdated` INTEGER NOT NULL, `longestSessionMillis` INTEGER NOT NULL, `shortestSessionMillis` INTEGER NOT NULL, `currentStreak` INTEGER NOT NULL, `longestStreak` INTEGER NOT NULL, PRIMARY KEY(`bookId`), FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bookmarks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bookId` INTEGER NOT NULL, `chapterIndex` INTEGER NOT NULL, `position` INTEGER NOT NULL, `chapterTitle` TEXT, `textPreview` TEXT, `note` TEXT, `createdDate` INTEGER NOT NULL, FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookmarks_bookId` ON `bookmarks` (`bookId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `highlights` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bookId` INTEGER NOT NULL, `chapterIndex` INTEGER NOT NULL, `startPosition` INTEGER NOT NULL, `endPosition` INTEGER NOT NULL, `highlightedText` TEXT NOT NULL, `color` TEXT NOT NULL, `note` TEXT, `createdDate` INTEGER NOT NULL, FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_highlights_bookId` ON `highlights` (`bookId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '86d51e9c88316771cb8a2ce4d6085356')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `books`");
        db.execSQL("DROP TABLE IF EXISTS `reading_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `reading_stats`");
        db.execSQL("DROP TABLE IF EXISTS `bookmarks`");
        db.execSQL("DROP TABLE IF EXISTS `highlights`");
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
        db.execSQL("PRAGMA foreign_keys = ON");
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
        final HashMap<String, TableInfo.Column> _columnsBooks = new HashMap<String, TableInfo.Column>(19);
        _columnsBooks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("author", new TableInfo.Column("author", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("filePath", new TableInfo.Column("filePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("coverImagePath", new TableInfo.Column("coverImagePath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("totalPages", new TableInfo.Column("totalPages", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("totalCharacters", new TableInfo.Column("totalCharacters", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("addedDate", new TableInfo.Column("addedDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("startDate", new TableInfo.Column("startDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("endDate", new TableInfo.Column("endDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("currentPosition", new TableInfo.Column("currentPosition", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("currentChapterIndex", new TableInfo.Column("currentChapterIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("currentChapterProgress", new TableInfo.Column("currentChapterProgress", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("completionPercentage", new TableInfo.Column("completionPercentage", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("isFavorite", new TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("lastReadDate", new TableInfo.Column("lastReadDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("fontSize", new TableInfo.Column("fontSize", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("isDarkMode", new TableInfo.Column("isDarkMode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBooks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBooks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBooks = new TableInfo("books", _columnsBooks, _foreignKeysBooks, _indicesBooks);
        final TableInfo _existingBooks = TableInfo.read(db, "books");
        if (!_infoBooks.equals(_existingBooks)) {
          return new RoomOpenHelper.ValidationResult(false, "books(com.example.epubreader.data.local.entity.Book).\n"
                  + " Expected:\n" + _infoBooks + "\n"
                  + " Found:\n" + _existingBooks);
        }
        final HashMap<String, TableInfo.Column> _columnsReadingSessions = new HashMap<String, TableInfo.Column>(9);
        _columnsReadingSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingSessions.put("bookId", new TableInfo.Column("bookId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingSessions.put("sessionStart", new TableInfo.Column("sessionStart", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingSessions.put("sessionEnd", new TableInfo.Column("sessionEnd", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingSessions.put("durationMillis", new TableInfo.Column("durationMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingSessions.put("pagesRead", new TableInfo.Column("pagesRead", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingSessions.put("startPosition", new TableInfo.Column("startPosition", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingSessions.put("endPosition", new TableInfo.Column("endPosition", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingSessions.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReadingSessions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysReadingSessions.add(new TableInfo.ForeignKey("books", "CASCADE", "NO ACTION", Arrays.asList("bookId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesReadingSessions = new HashSet<TableInfo.Index>(2);
        _indicesReadingSessions.add(new TableInfo.Index("index_reading_sessions_bookId", false, Arrays.asList("bookId"), Arrays.asList("ASC")));
        _indicesReadingSessions.add(new TableInfo.Index("index_reading_sessions_sessionStart", false, Arrays.asList("sessionStart"), Arrays.asList("ASC")));
        final TableInfo _infoReadingSessions = new TableInfo("reading_sessions", _columnsReadingSessions, _foreignKeysReadingSessions, _indicesReadingSessions);
        final TableInfo _existingReadingSessions = TableInfo.read(db, "reading_sessions");
        if (!_infoReadingSessions.equals(_existingReadingSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "reading_sessions(com.example.epubreader.data.local.entity.ReadingSession).\n"
                  + " Expected:\n" + _infoReadingSessions + "\n"
                  + " Found:\n" + _existingReadingSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsReadingStats = new HashMap<String, TableInfo.Column>(13);
        _columnsReadingStats.put("bookId", new TableInfo.Column("bookId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("totalReadingTimeMillis", new TableInfo.Column("totalReadingTimeMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("totalSessions", new TableInfo.Column("totalSessions", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("averageSessionDurationMillis", new TableInfo.Column("averageSessionDurationMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("averagePagesPerMinute", new TableInfo.Column("averagePagesPerMinute", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("averageWordsPerMinute", new TableInfo.Column("averageWordsPerMinute", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("completionPercentage", new TableInfo.Column("completionPercentage", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("daysToComplete", new TableInfo.Column("daysToComplete", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("longestSessionMillis", new TableInfo.Column("longestSessionMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("shortestSessionMillis", new TableInfo.Column("shortestSessionMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("currentStreak", new TableInfo.Column("currentStreak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReadingStats.put("longestStreak", new TableInfo.Column("longestStreak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReadingStats = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysReadingStats.add(new TableInfo.ForeignKey("books", "CASCADE", "NO ACTION", Arrays.asList("bookId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesReadingStats = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReadingStats = new TableInfo("reading_stats", _columnsReadingStats, _foreignKeysReadingStats, _indicesReadingStats);
        final TableInfo _existingReadingStats = TableInfo.read(db, "reading_stats");
        if (!_infoReadingStats.equals(_existingReadingStats)) {
          return new RoomOpenHelper.ValidationResult(false, "reading_stats(com.example.epubreader.data.local.entity.ReadingStats).\n"
                  + " Expected:\n" + _infoReadingStats + "\n"
                  + " Found:\n" + _existingReadingStats);
        }
        final HashMap<String, TableInfo.Column> _columnsBookmarks = new HashMap<String, TableInfo.Column>(8);
        _columnsBookmarks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("bookId", new TableInfo.Column("bookId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("chapterIndex", new TableInfo.Column("chapterIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("position", new TableInfo.Column("position", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("chapterTitle", new TableInfo.Column("chapterTitle", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("textPreview", new TableInfo.Column("textPreview", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookmarks.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBookmarks = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBookmarks.add(new TableInfo.ForeignKey("books", "CASCADE", "NO ACTION", Arrays.asList("bookId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBookmarks = new HashSet<TableInfo.Index>(1);
        _indicesBookmarks.add(new TableInfo.Index("index_bookmarks_bookId", false, Arrays.asList("bookId"), Arrays.asList("ASC")));
        final TableInfo _infoBookmarks = new TableInfo("bookmarks", _columnsBookmarks, _foreignKeysBookmarks, _indicesBookmarks);
        final TableInfo _existingBookmarks = TableInfo.read(db, "bookmarks");
        if (!_infoBookmarks.equals(_existingBookmarks)) {
          return new RoomOpenHelper.ValidationResult(false, "bookmarks(com.example.epubreader.data.local.entity.Bookmark).\n"
                  + " Expected:\n" + _infoBookmarks + "\n"
                  + " Found:\n" + _existingBookmarks);
        }
        final HashMap<String, TableInfo.Column> _columnsHighlights = new HashMap<String, TableInfo.Column>(9);
        _columnsHighlights.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHighlights.put("bookId", new TableInfo.Column("bookId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHighlights.put("chapterIndex", new TableInfo.Column("chapterIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHighlights.put("startPosition", new TableInfo.Column("startPosition", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHighlights.put("endPosition", new TableInfo.Column("endPosition", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHighlights.put("highlightedText", new TableInfo.Column("highlightedText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHighlights.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHighlights.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHighlights.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHighlights = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysHighlights.add(new TableInfo.ForeignKey("books", "CASCADE", "NO ACTION", Arrays.asList("bookId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesHighlights = new HashSet<TableInfo.Index>(1);
        _indicesHighlights.add(new TableInfo.Index("index_highlights_bookId", false, Arrays.asList("bookId"), Arrays.asList("ASC")));
        final TableInfo _infoHighlights = new TableInfo("highlights", _columnsHighlights, _foreignKeysHighlights, _indicesHighlights);
        final TableInfo _existingHighlights = TableInfo.read(db, "highlights");
        if (!_infoHighlights.equals(_existingHighlights)) {
          return new RoomOpenHelper.ValidationResult(false, "highlights(com.example.epubreader.data.local.entity.Highlight).\n"
                  + " Expected:\n" + _infoHighlights + "\n"
                  + " Found:\n" + _existingHighlights);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "86d51e9c88316771cb8a2ce4d6085356", "efd0c54133defb65b4dbe824ff1508aa");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "books","reading_sessions","reading_stats","bookmarks","highlights");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `books`");
      _db.execSQL("DELETE FROM `reading_sessions`");
      _db.execSQL("DELETE FROM `reading_stats`");
      _db.execSQL("DELETE FROM `bookmarks`");
      _db.execSQL("DELETE FROM `highlights`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
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
    _typeConvertersMap.put(BookDao.class, BookDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReadingSessionDao.class, ReadingSessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReadingStatsDao.class, ReadingStatsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BookmarkDao.class, BookmarkDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HighlightDao.class, HighlightDao_Impl.getRequiredConverters());
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
  public BookDao bookDao() {
    if (_bookDao != null) {
      return _bookDao;
    } else {
      synchronized(this) {
        if(_bookDao == null) {
          _bookDao = new BookDao_Impl(this);
        }
        return _bookDao;
      }
    }
  }

  @Override
  public ReadingSessionDao readingSessionDao() {
    if (_readingSessionDao != null) {
      return _readingSessionDao;
    } else {
      synchronized(this) {
        if(_readingSessionDao == null) {
          _readingSessionDao = new ReadingSessionDao_Impl(this);
        }
        return _readingSessionDao;
      }
    }
  }

  @Override
  public ReadingStatsDao readingStatsDao() {
    if (_readingStatsDao != null) {
      return _readingStatsDao;
    } else {
      synchronized(this) {
        if(_readingStatsDao == null) {
          _readingStatsDao = new ReadingStatsDao_Impl(this);
        }
        return _readingStatsDao;
      }
    }
  }

  @Override
  public BookmarkDao bookmarkDao() {
    if (_bookmarkDao != null) {
      return _bookmarkDao;
    } else {
      synchronized(this) {
        if(_bookmarkDao == null) {
          _bookmarkDao = new BookmarkDao_Impl(this);
        }
        return _bookmarkDao;
      }
    }
  }

  @Override
  public HighlightDao highlightDao() {
    if (_highlightDao != null) {
      return _highlightDao;
    } else {
      synchronized(this) {
        if(_highlightDao == null) {
          _highlightDao = new HighlightDao_Impl(this);
        }
        return _highlightDao;
      }
    }
  }
}
