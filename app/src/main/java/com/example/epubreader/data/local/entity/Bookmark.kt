package com.example.epubreader.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entità Bookmark - Segnalibri dell'utente
 * 
 * Permette all'utente di salvare posizioni specifiche nel libro
 */
@Entity(
    tableName = "bookmarks",
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
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val bookId: Long,
    
    // Posizione del segnalibro
    val chapterIndex: Int,
    val position: Int,
    val chapterTitle: String? = null,
    
    // Testo estratto intorno al segnalibro (per preview)
    val textPreview: String? = null,
    
    // Note dell'utente
    val note: String? = null,
    
    // Data creazione
    val createdDate: Long = System.currentTimeMillis()
)
