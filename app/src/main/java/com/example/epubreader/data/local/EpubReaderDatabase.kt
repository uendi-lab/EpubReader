package com.example.epubreader.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.epubreader.data.local.dao.*
import com.example.epubreader.data.local.entity.*

/**
 * Database Room principale dell'applicazione
 * 
 * Contiene tutte le entità e fornisce accesso ai DAO
 */
@Database(
    entities = [
        Book::class,
        ReadingSession::class,
        ReadingStats::class,
        Bookmark::class,
        Highlight::class
    ],
    version = 1,
    exportSchema = true
)
abstract class EpubReaderDatabase : RoomDatabase() {
    
    abstract fun bookDao(): BookDao
    abstract fun readingSessionDao(): ReadingSessionDao
    abstract fun readingStatsDao(): ReadingStatsDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun highlightDao(): HighlightDao
    
    companion object {
        @Volatile
        private var INSTANCE: EpubReaderDatabase? = null
        
        private const val DATABASE_NAME = "epub_reader_database"
        
        /**
         * Ottiene l'istanza singleton del database
         */
        fun getInstance(context: Context): EpubReaderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EpubReaderDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                
                INSTANCE = instance
                instance
            }
        }
        
        /**
         * Chiude il database (usato principalmente per testing)
         */
        fun closeDatabase() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}
