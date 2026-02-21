package com.example.epubreader.data.repository

import com.example.epubreader.data.local.dao.ReadingSessionDao
import com.example.epubreader.data.local.entity.ReadingSession
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

/**
 * Repository per gestione delle sessioni di lettura
 * 
 * Implementa il tracking AUTOMATICO delle sessioni:
 * - Start automatico quando si apre la schermata di lettura
 * - Pausa automatica quando si va in background
 * - Resume automatico quando si torna nella schermata
 * - Stop automatico quando si chiude il libro
 */
class ReadingSessionRepository(private val sessionDao: ReadingSessionDao) {
    
    /**
     * Avvia una nuova sessione di lettura
     * Chiamata automaticamente quando l'utente entra nella schermata di lettura
     */
    suspend fun startSession(bookId: Long, startPosition: Int): Long {
        // Verifica se esiste già una sessione attiva per questo libro
        val existingSession = sessionDao.getActiveSession(bookId)
        if (existingSession != null) {
            return existingSession.id
        }
        
        val session = ReadingSession(
            bookId = bookId,
            sessionStart = System.currentTimeMillis(),
            startPosition = startPosition,
            isActive = true
        )
        
        return sessionDao.insertSession(session)
    }
    
    /**
     * Chiude la sessione attiva
     * Chiamata automaticamente quando l'utente esce dalla schermata di lettura
     */
    suspend fun endSession(bookId: Long, endPosition: Int, pagesRead: Int) {
        val session = sessionDao.getActiveSession(bookId) ?: return
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - session.sessionStart
        
        sessionDao.closeSession(
            sessionId = session.id,
            endTime = endTime,
            duration = duration,
            endPosition = endPosition,
            pagesRead = pagesRead
        )
    }
    
    /**
     * Ottiene la sessione attiva per un libro (se esiste)
     */
    suspend fun getActiveSession(bookId: Long): ReadingSession? {
        return sessionDao.getActiveSession(bookId)
    }
    
    /**
     * Ottiene tutte le sessioni per un libro
     */
    fun getSessionsForBook(bookId: Long): Flow<List<ReadingSession>> {
        return sessionDao.getSessionsForBook(bookId)
    }
    
    /**
     * Ottiene le sessioni di oggi
     */
    fun getTodaySessions(): Flow<List<ReadingSession>> {
        val todayStart = getTodayStartMillis()
        return sessionDao.getTodaySessions(todayStart)
    }
    
    /**
     * Calcola il tempo totale di lettura per un libro
     */
    suspend fun getTotalReadingTimeForBook(bookId: Long): Long {
        return sessionDao.getTotalReadingTimeForBook(bookId) ?: 0L
    }
    
    /**
     * Calcola il tempo totale di lettura globale
     */
    suspend fun getTotalReadingTime(): Long {
        return sessionDao.getTotalReadingTime() ?: 0L
    }
    
    /**
     * Calcola il tempo di lettura di oggi
     */
    suspend fun getTodayReadingTime(): Long {
        val todayStart = getTodayStartMillis()
        return sessionDao.getTodayReadingTime(todayStart) ?: 0L
    }
    
    /**
     * Calcola il tempo di lettura del mese corrente
     */
    suspend fun getMonthReadingTime(): Long {
        val monthStart = getMonthStartMillis()
        return sessionDao.getMonthReadingTime(monthStart) ?: 0L
    }
    
    /**
     * Ottiene le sessioni in un range di date
     */
    suspend fun getSessionsInDateRange(startDate: Long, endDate: Long): List<ReadingSession> {
        return sessionDao.getSessionsInDateRange(startDate, endDate)
    }
    
    /**
     * Conta le sessioni per un libro
     */
    suspend fun getSessionCountForBook(bookId: Long): Int {
        return sessionDao.getSessionCountForBook(bookId)
    }
    
    /**
     * Ottiene l'ultima sessione completata per un libro
     */
    suspend fun getLastCompletedSession(bookId: Long): ReadingSession? {
        return sessionDao.getLastCompletedSession(bookId)
    }
    
    /**
     * Elimina tutte le sessioni di un libro
     */
    suspend fun deleteSessionsForBook(bookId: Long) {
        sessionDao.deleteSessionsForBook(bookId)
    }
    
    /**
     * Calcola le sessioni per gli ultimi 7 giorni (per grafico)
     */
    suspend fun getWeeklyReadingSessions(): Map<Long, Long> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startDate = calendar.timeInMillis
        
        val sessions = sessionDao.getSessionsInDateRange(startDate, endDate)
        
        // Raggruppa per giorno
        val dailyMap = mutableMapOf<Long, Long>()
        sessions.forEach { session ->
            val dayStart = getDayStartMillis(session.sessionStart)
            dailyMap[dayStart] = (dailyMap[dayStart] ?: 0L) + session.durationMillis
        }
        
        return dailyMap
    }
    
    // Utility functions
    
    private fun getTodayStartMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    private fun getMonthStartMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    private fun getDayStartMillis(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
