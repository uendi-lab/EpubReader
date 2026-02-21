package com.example.epubreader.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entità ReadingSession - Rappresenta una singola sessione di lettura
 * 
 * Traccia automaticamente ogni volta che l'utente apre e legge un libro.
 * La sessione inizia quando entra nella schermata di lettura e termina quando esce.
 */
@Entity(
    tableName = "reading_sessions",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bookId"), Index("sessionStart")]
)
data class ReadingSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val bookId: Long,
    
    // Timestamp inizio sessione (millisecondi)
    val sessionStart: Long,
    
    // Timestamp fine sessione (millisecondi)
    val sessionEnd: Long? = null,
    
    // Durata effettiva della sessione in millisecondi
    // Calcolata considerando pause quando app va in background
    val durationMillis: Long = 0,
    
    // Pagine lette durante questa sessione
    val pagesRead: Int = 0,
    
    // Posizione iniziale e finale nel libro
    val startPosition: Int = 0,
    val endPosition: Int = 0,
    
    // Flag per sessione ancora attiva
    val isActive: Boolean = false
)
