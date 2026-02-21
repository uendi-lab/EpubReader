package com.example.epubreader.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epubreader.data.local.entity.Book
import com.example.epubreader.data.local.entity.ReadingStats
import com.example.epubreader.data.repository.BookRepository
import com.example.epubreader.data.repository.ReadingSessionRepository
import com.example.epubreader.data.repository.ReadingStatsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel per la schermata Statistiche
 * 
 * Mostra statistiche aggregate di lettura
 */
class StatsViewModel(
    private val bookRepository: BookRepository,
    private val sessionRepository: ReadingSessionRepository,
    private val statsRepository: ReadingStatsRepository
) : ViewModel() {
    
    /**
     * Dati statistici aggregati
     */
    data class AggregatedStats(
        val todayReadingTime: Long = 0L,
        val monthReadingTime: Long = 0L,
        val totalReadingTime: Long = 0L,
        val totalBooks: Int = 0,
        val completedBooks: Int = 0,
        val fastestReadBook: BookWithStats? = null,
        val averageDailyMinutes: Long = 0L,
        val weeklyReadingData: Map<String, Long> = emptyMap()
    )
    
    data class BookWithStats(
        val book: Book,
        val stats: ReadingStats
    )
    
    // Statistiche aggregate
    private val _aggregatedStats = MutableStateFlow(AggregatedStats())
    val aggregatedStats: StateFlow<AggregatedStats> = _aggregatedStats.asStateFlow()
    
    // Tutte le statistiche per libro
    val allBookStats: StateFlow<List<ReadingStats>> = statsRepository.getAllStats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    init {
        loadStats()
    }
    
    /**
     * Carica tutte le statistiche
     */
    private fun loadStats() {
        viewModelScope.launch {
            try {
                // Tempo di lettura oggi
                val todayTime = sessionRepository.getTodayReadingTime()
                
                // Tempo di lettura questo mese
                val monthTime = sessionRepository.getMonthReadingTime()
                
                // Tempo totale di lettura
                val totalTime = sessionRepository.getTotalReadingTime()
                
                // Conta libri
                val totalBooks = bookRepository.getTotalBooksCount().first()
                val completedBooks = bookRepository.getCompletedBooksCount().first()
                
                // Libro più veloce
                val fastestStats = statsRepository.getFastestReadBook()
                val fastestBook = fastestStats?.let {
                    val book = bookRepository.getBookByIdSync(it.bookId)
                    if (book != null) BookWithStats(book, it) else null
                }
                
                // Media minuti giornalieri
                val avgDaily = statsRepository.getAverageDailyReadingTime()
                val avgDailyMinutes = avgDaily / (1000 * 60)
                
                // Dati settimanali per grafico
                val weeklyData = sessionRepository.getWeeklyReadingSessions()
                val weeklyFormatted = formatWeeklyData(weeklyData)
                
                _aggregatedStats.value = AggregatedStats(
                    todayReadingTime = todayTime,
                    monthReadingTime = monthTime,
                    totalReadingTime = totalTime,
                    totalBooks = totalBooks,
                    completedBooks = completedBooks,
                    fastestReadBook = fastestBook,
                    averageDailyMinutes = avgDailyMinutes,
                    weeklyReadingData = weeklyFormatted
                )
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Formatta i dati settimanali per il grafico
     */
    private fun formatWeeklyData(weeklyData: Map<Long, Long>): Map<String, Long> {
        val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val formatted = mutableMapOf<String, Long>()
        
        weeklyData.forEach { (timestamp, duration) ->
            val date = Date(timestamp)
            val dayLabel = dateFormat.format(date)
            formatted[dayLabel] = duration / (1000 * 60) // Converti in minuti
        }
        
        return formatted
    }
    
    /**
     * Formatta millisecondi in stringa leggibile
     */
    fun formatDuration(millis: Long): String {
        val hours = millis / (1000 * 60 * 60)
        val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)
        
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m"
            else -> "< 1m"
        }
    }
    
    /**
     * Ricarica le statistiche
     */
    fun refresh() {
        loadStats()
    }
}
