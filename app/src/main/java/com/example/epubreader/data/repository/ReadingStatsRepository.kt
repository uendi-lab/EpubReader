package com.example.epubreader.data.repository

import com.example.epubreader.data.local.dao.ReadingSessionDao
import com.example.epubreader.data.local.dao.ReadingStatsDao
import com.example.epubreader.data.local.entity.ReadingSession
import com.example.epubreader.data.local.entity.ReadingStats
import kotlinx.coroutines.flow.Flow

/**
 * Repository per gestione delle statistiche di lettura
 * 
 * Calcola automaticamente le statistiche basandosi sulle sessioni di lettura
 */
class ReadingStatsRepository(
    private val statsDao: ReadingStatsDao,
    private val sessionDao: ReadingSessionDao
) {
    
    /**
     * Ottiene le statistiche per un libro
     */
    fun getStatsForBook(bookId: Long): Flow<ReadingStats?> {
        return statsDao.getStatsForBook(bookId)
    }
    
    /**
     * Ottiene tutte le statistiche
     */
    fun getAllStats(): Flow<List<ReadingStats>> {
        return statsDao.getAllStats()
    }
    
    /**
     * Ricalcola e aggiorna le statistiche per un libro
     * Chiamata automaticamente dopo ogni sessione completata
     */
    suspend fun recalculateStatsForBook(bookId: Long, completionPercentage: Float) {
        val sessions = sessionDao.getSessionsForBookSync(bookId)
            .filter { !it.isActive } // Solo sessioni completate
        
        if (sessions.isEmpty()) {
            return
        }
        
        // Calcola statistiche
        val totalTime = sessions.sumOf { it.durationMillis }
        val totalSessions = sessions.size
        val avgDuration = totalTime / totalSessions
        val totalPagesRead = sessions.sumOf { it.pagesRead }
        
        // Velocità di lettura (pagine al minuto)
        val totalMinutes = totalTime / 60000.0
        val avgPagesPerMinute = if (totalMinutes > 0) {
            (totalPagesRead / totalMinutes).toFloat()
        } else 0f
        
        // Stima parole al minuto (assumendo ~250 parole per pagina)
        val avgWordsPerMinute = avgPagesPerMinute * 250
        
        // Sessione più lunga e più breve
        val longestSession = sessions.maxOfOrNull { it.durationMillis } ?: 0L
        val shortestSession = sessions.minOfOrNull { it.durationMillis } ?: 0L
        
        // Giorni per completamento (se completato)
        val daysToComplete = calculateDaysToComplete(sessions, completionPercentage)
        
        // Crea o aggiorna le statistiche
        val existingStats = statsDao.getStatsForBookSync(bookId)
        val stats = if (existingStats != null) {
            existingStats.copy(
                totalReadingTimeMillis = totalTime,
                totalSessions = totalSessions,
                averageSessionDurationMillis = avgDuration,
                averagePagesPerMinute = avgPagesPerMinute,
                averageWordsPerMinute = avgWordsPerMinute,
                completionPercentage = completionPercentage,
                daysToComplete = daysToComplete,
                longestSessionMillis = longestSession,
                shortestSessionMillis = shortestSession,
                lastUpdated = System.currentTimeMillis()
            )
        } else {
            ReadingStats(
                bookId = bookId,
                totalReadingTimeMillis = totalTime,
                totalSessions = totalSessions,
                averageSessionDurationMillis = avgDuration,
                averagePagesPerMinute = avgPagesPerMinute,
                averageWordsPerMinute = avgWordsPerMinute,
                completionPercentage = completionPercentage,
                daysToComplete = daysToComplete,
                longestSessionMillis = longestSession,
                shortestSessionMillis = shortestSession,
                lastUpdated = System.currentTimeMillis()
            )
        }
        
        statsDao.insertStats(stats)
    }
    
    /**
     * Calcola i giorni impiegati per completare il libro
     */
    private fun calculateDaysToComplete(
        sessions: List<ReadingSession>,
        completionPercentage: Float
    ): Int? {
        if (completionPercentage < 95f) return null
        
        val firstSession = sessions.minByOrNull { it.sessionStart } ?: return null
        val lastSession = sessions.maxByOrNull { it.sessionEnd ?: 0L } ?: return null
        
        val endTime = lastSession.sessionEnd ?: return null
        val daysDiff = (endTime - firstSession.sessionStart) / (1000 * 60 * 60 * 24)
        
        return daysDiff.toInt() + 1 // +1 per includere il primo giorno
    }
    
    /**
     * Ottiene il libro letto più velocemente
     */
    suspend fun getFastestReadBook(): ReadingStats? {
        return statsDao.getFastestReadBook()
    }
    
    /**
     * Ottiene la media di minuti al giorno
     */
    suspend fun getAverageDailyReadingTime(): Long {
        return statsDao.getAverageDailyReadingTime() ?: 0L
    }
    
    /**
     * Aggiorna lo streak di lettura per un libro
     */
    suspend fun updateReadingStreak(bookId: Long) {
        val sessions = sessionDao.getSessionsForBookSync(bookId)
            .filter { !it.isActive }
            .sortedByDescending { it.sessionStart }
        
        if (sessions.isEmpty()) return
        
        val currentStreak = calculateCurrentStreak(sessions)
        statsDao.updateStreak(bookId, currentStreak)
    }
    
    /**
     * Calcola lo streak corrente (giorni consecutivi di lettura)
     */
    private fun calculateCurrentStreak(sessions: List<ReadingSession>): Int {
        if (sessions.isEmpty()) return 0
        
        val daysWithReading = sessions.map { session ->
            val dayStart = getDayStart(session.sessionStart)
            dayStart
        }.distinct().sorted()
        
        if (daysWithReading.isEmpty()) return 0
        
        var streak = 1
        for (i in daysWithReading.size - 1 downTo 1) {
            val diff = (daysWithReading[i] - daysWithReading[i - 1]) / (1000 * 60 * 60 * 24)
            if (diff == 1L) {
                streak++
            } else {
                break
            }
        }
        
        return streak
    }
    
    private fun getDayStart(timestamp: Long): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    /**
     * Elimina le statistiche per un libro
     */
    suspend fun deleteStatsForBook(bookId: Long) {
        statsDao.deleteStatsForBook(bookId)
    }
}
