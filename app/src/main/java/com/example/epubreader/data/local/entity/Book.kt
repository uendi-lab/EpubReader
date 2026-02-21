package com.example.epubreader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entità Book - Rappresenta un libro nella libreria
 * 
 * Contiene tutte le informazioni relative al libro e al suo stato di lettura
 */
@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // Informazioni libro
    val title: String,
    val author: String,
    val filePath: String,  // URI persistente del file EPUB
    val coverImagePath: String? = null,  // Path dell'immagine di copertina estratta
    val totalPages: Int = 0,  // Numero stimato di pagine o capitoli
    val totalCharacters: Long = 0,  // Numero totale di caratteri (per calcolo avanzamento)
    
    // Date tracking
    val addedDate: Long = System.currentTimeMillis(),
    val startDate: Long? = null,  // Data di prima apertura
    val endDate: Long? = null,  // Data di completamento (>= 95%)
    
    // Progresso lettura
    val currentPosition: Int = 0,  // Posizione corrente (capitolo o offset)
    val currentChapterIndex: Int = 0,  // Indice del capitolo corrente
    val currentChapterProgress: Float = 0f,  // Progresso all'interno del capitolo (0-1)
    val completionPercentage: Float = 0f,  // Percentuale completamento totale (0-100)
    
    // Stato
    val isCompleted: Boolean = false,  // True se >= 95% completato
    val isFavorite: Boolean = false,
    
    // Ultima lettura
    val lastReadDate: Long? = null,
    
    // Impostazioni di lettura personalizzate per questo libro
    val fontSize: Float = 16f,
    val isDarkMode: Boolean = false
)
