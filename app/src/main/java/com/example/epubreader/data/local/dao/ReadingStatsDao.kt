package com.example.epubreader.data.local.dao

import androidx.room.*
import com.example.epubreader.data.local.entity.ReadingStats
import kotlinx.coroutines.flow.Flow

/**
 * DAO per operazioni su ReadingStats
 * 
 * Gestisce le statistiche aggregate di lettura
 */
@Dao
interface ReadingStatsDao {
    
    @Query("SELECT * FROM reading_stats WHERE bookId = :bookId")
    fun getStatsForBook(bookId: Long): Flow<ReadingStats?>
    
    @Query("SELECT * FROM reading_stats WHERE bookId = :bookId")
    suspend fun getStatsForBookSync(bookId: Long): ReadingStats?
    
    @Query("SELECT * FROM reading_stats")
    fun getAllStats(): Flow<List<ReadingStats>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: ReadingStats)
    
    @Update
    suspend fun updateStats(stats: ReadingStats)
    
    @Delete
    suspend fun deleteStats(stats: ReadingStats)
    
    @Query("DELETE FROM reading_stats WHERE bookId = :bookId")
    suspend fun deleteStatsForBook(bookId: Long)
    
    /**
     * Aggiorna le statistiche per un libro
     */
    @Query("""
        UPDATE reading_stats 
        SET totalReadingTimeMillis = :totalTime,
            totalSessions = :totalSessions,
            averageSessionDurationMillis = :avgDuration,
            averagePagesPerMinute = :avgPagesPerMin,
            averageWordsPerMinute = :avgWordsPerMin,
            completionPercentage = :completion,
            daysToComplete = :daysToComplete,
            longestSessionMillis = :longestSession,
            shortestSessionMillis = :shortestSession,
            lastUpdated = :lastUpdated
        WHERE bookId = :bookId
    """)
    suspend fun updateBookStats(
        bookId: Long,
        totalTime: Long,
        totalSessions: Int,
        avgDuration: Long,
        avgPagesPerMin: Float,
        avgWordsPerMin: Float,
        completion: Float,
        daysToComplete: Int?,
        longestSession: Long,
        shortestSession: Long,
        lastUpdated: Long
    )
    
    /**
     * Ottiene il libro letto più velocemente (min tempo per completamento)
     */
    @Query("""
        SELECT * FROM reading_stats 
        WHERE completionPercentage >= 95 AND totalReadingTimeMillis > 0
        ORDER BY totalReadingTimeMillis ASC 
        LIMIT 1
    """)
    suspend fun getFastestReadBook(): ReadingStats?
    
    /**
     * Ottiene tempo medio di lettura giornaliero
     */
    @Query("""
        SELECT AVG(totalReadingTimeMillis / NULLIF(daysToComplete, 0)) 
        FROM reading_stats 
        WHERE daysToComplete > 0
    """)
    suspend fun getAverageDailyReadingTime(): Long?
    
    /**
     * Aggiorna lo streak di lettura
     */
    @Query("""
        UPDATE reading_stats 
        SET currentStreak = :currentStreak,
            longestStreak = CASE WHEN :currentStreak > longestStreak 
                                  THEN :currentStreak 
                                  ELSE longestStreak END
        WHERE bookId = :bookId
    """)
    suspend fun updateStreak(bookId: Long, currentStreak: Int)
}
