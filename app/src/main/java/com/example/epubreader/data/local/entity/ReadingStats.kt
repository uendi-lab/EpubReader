package com.example.epubreader.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entità ReadingStats - Statistiche aggregate per libro
 * 
 * Mantiene statistiche calcolate automaticamente basate sulle sessioni di lettura.
 * Viene aggiornata automaticamente ad ogni sessione completata.
 */
@Entity(
    tableName = "reading_stats",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReadingStats(
    @PrimaryKey
    val bookId: Long,
    
    // Tempo totale di lettura in millisecondi
    val totalReadingTimeMillis: Long = 0,
    
    // Numero totale di sessioni
    val totalSessions: Int = 0,
    
    // Durata media delle sessioni in millisecondi
    val averageSessionDurationMillis: Long = 0,
    
    // Velocità media di lettura (pagine al minuto)
    val averagePagesPerMinute: Float = 0f,
    
    // Velocità media di lettura (parole al minuto)
    val averageWordsPerMinute: Float = 0f,
    
    // Percentuale di completamento (0-100)
    val completionPercentage: Float = 0f,
    
    // Giorni impiegati per completare il libro
    val daysToComplete: Int? = null,
    
    // Ultima volta che le statistiche sono state aggiornate
    val lastUpdated: Long = System.currentTimeMillis(),
    
    // Sessione più lunga in millisecondi
    val longestSessionMillis: Long = 0,
    
    // Sessione più breve in millisecondi
    val shortestSessionMillis: Long = 0,
    
    // Streak di lettura (giorni consecutivi)
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)
