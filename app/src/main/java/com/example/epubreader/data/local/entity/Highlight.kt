package com.example.epubreader.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entità Highlight - Evidenziazioni di testo
 * 
 * Permette all'utente di evidenziare porzioni di testo
 */
@Entity(
    tableName = "highlights",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bookId")]
)
data class Highlight(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val bookId: Long,
    
    // Posizione dell'evidenziazione
    val chapterIndex: Int,
    val startPosition: Int,
    val endPosition: Int,
    
    // Testo evidenziato
    val highlightedText: String,
    
    // Colore evidenziazione (formato hex, es. "#FFFF00")
    val color: String = "#FFFF00",
    
    // Note associate
    val note: String? = null,
    
    // Data creazione
    val createdDate: Long = System.currentTimeMillis()
)
