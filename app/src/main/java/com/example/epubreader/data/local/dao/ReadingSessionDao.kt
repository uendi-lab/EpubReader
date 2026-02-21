package com.example.epubreader.data.local.dao

import androidx.room.*
import com.example.epubreader.data.local.entity.ReadingSession
import kotlinx.coroutines.flow.Flow

/**
 * DAO per operazioni su ReadingSession
 * 
 * Gestisce il tracking automatico delle sessioni di lettura
 */
@Dao
interface ReadingSessionDao {
    
    @Query("SELECT * FROM reading_sessions WHERE bookId = :bookId ORDER BY sessionStart DESC")
    fun getSessionsForBook(bookId: Long): Flow<List<ReadingSession>>
    
    @Query("SELECT * FROM reading_sessions WHERE bookId = :bookId ORDER BY sessionStart DESC")
    suspend fun getSessionsForBookSync(bookId: Long): List<ReadingSession>
    
    @Query("SELECT * FROM reading_sessions WHERE isActive = 1 AND bookId = :bookId LIMIT 1")
    suspend fun getActiveSession(bookId: Long): ReadingSession?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ReadingSession): Long
    
    @Update
    suspend fun updateSession(session: ReadingSession)
    
    @Delete
    suspend fun deleteSession(session: ReadingSession)
    
    /**
     * Chiude la sessione attiva per un libro
     */
    @Query("""
        UPDATE reading_sessions 
        SET sessionEnd = :endTime,
            durationMillis = :duration,
            endPosition = :endPosition,
            pagesRead = :pagesRead,
            isActive = 0
        WHERE id = :sessionId
    """)
    suspend fun closeSession(
        sessionId: Long,
        endTime: Long,
        duration: Long,
        endPosition: Int,
        pagesRead: Int
    )
    
    /**
     * Ottiene tutte le sessioni in un range di date
     */
    @Query("""
        SELECT * FROM reading_sessions 
        WHERE sessionStart >= :startDate AND sessionStart <= :endDate
        ORDER BY sessionStart DESC
    """)
    suspend fun getSessionsInDateRange(startDate: Long, endDate: Long): List<ReadingSession>
    
    /**
     * Ottiene le sessioni di oggi
     */
    @Query("""
        SELECT * FROM reading_sessions 
        WHERE sessionStart >= :todayStart
        ORDER BY sessionStart DESC
    """)
    fun getTodaySessions(todayStart: Long): Flow<List<ReadingSession>>
    
    /**
     * Calcola tempo totale di lettura per un libro
     */
    @Query("SELECT SUM(durationMillis) FROM reading_sessions WHERE bookId = :bookId AND isActive = 0")
    suspend fun getTotalReadingTimeForBook(bookId: Long): Long?
    
    /**
     * Calcola tempo totale di lettura globale
     */
    @Query("SELECT SUM(durationMillis) FROM reading_sessions WHERE isActive = 0")
    suspend fun getTotalReadingTime(): Long?
    
    /**
     * Calcola tempo totale di lettura di oggi
     */
    @Query("""
        SELECT SUM(durationMillis) FROM reading_sessions 
        WHERE sessionStart >= :todayStart AND isActive = 0
    """)
    suspend fun getTodayReadingTime(todayStart: Long): Long?
    
    /**
     * Calcola tempo totale di lettura del mese corrente
     */
    @Query("""
        SELECT SUM(durationMillis) FROM reading_sessions 
        WHERE sessionStart >= :monthStart AND isActive = 0
    """)
    suspend fun getMonthReadingTime(monthStart: Long): Long?
    
    /**
     * Conta sessioni per libro
     */
    @Query("SELECT COUNT(*) FROM reading_sessions WHERE bookId = :bookId AND isActive = 0")
    suspend fun getSessionCountForBook(bookId: Long): Int
    
    /**
     * Ottiene l'ultima sessione completata per un libro
     */
    @Query("""
        SELECT * FROM reading_sessions 
        WHERE bookId = :bookId AND isActive = 0 
        ORDER BY sessionEnd DESC 
        LIMIT 1
    """)
    suspend fun getLastCompletedSession(bookId: Long): ReadingSession?
    
    /**
     * Elimina tutte le sessioni di un libro
     */
    @Query("DELETE FROM reading_sessions WHERE bookId = :bookId")
    suspend fun deleteSessionsForBook(bookId: Long)
}
