package com.example.epubreader.data.local.dao

import androidx.room.*
import com.example.epubreader.data.local.entity.Book
import kotlinx.coroutines.flow.Flow

/**
 * DAO per operazioni su Book
 * 
 * Gestisce tutte le query relative ai libri
 */
@Dao
interface BookDao {
    
    @Query("SELECT * FROM books ORDER BY lastReadDate DESC")
    fun getAllBooks(): Flow<List<Book>>
    
    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookById(bookId: Long): Flow<Book?>
    
    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookByIdSync(bookId: Long): Book?
    
    @Query("SELECT * FROM books WHERE isCompleted = 0 ORDER BY lastReadDate DESC")
    fun getInProgressBooks(): Flow<List<Book>>
    
    @Query("SELECT * FROM books WHERE isCompleted = 1 ORDER BY endDate DESC")
    fun getCompletedBooks(): Flow<List<Book>>
    
    @Query("SELECT * FROM books WHERE isFavorite = 1 ORDER BY lastReadDate DESC")
    fun getFavoriteBooks(): Flow<List<Book>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long
    
    @Update
    suspend fun updateBook(book: Book)
    
    @Delete
    suspend fun deleteBook(book: Book)
    
    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: Long)
    
    /**
     * Aggiorna il progresso di lettura del libro
     */
    @Query("""
        UPDATE books 
        SET currentPosition = :position,
            currentChapterIndex = :chapterIndex,
            currentChapterProgress = :chapterProgress,
            completionPercentage = :completionPercentage,
            lastReadDate = :lastReadDate,
            isCompleted = :isCompleted,
            endDate = :endDate
        WHERE id = :bookId
    """)
    suspend fun updateReadingProgress(
        bookId: Long,
        position: Int,
        chapterIndex: Int,
        chapterProgress: Float,
        completionPercentage: Float,
        lastReadDate: Long,
        isCompleted: Boolean,
        endDate: Long?
    )
    
    /**
     * Imposta la data di inizio lettura (solo se non già impostata)
     */
    @Query("UPDATE books SET startDate = :startDate WHERE id = :bookId AND startDate IS NULL")
    suspend fun setStartDateIfNull(bookId: Long, startDate: Long)
    
    /**
     * Aggiorna le impostazioni di lettura
     */
    @Query("UPDATE books SET fontSize = :fontSize, isDarkMode = :isDarkMode WHERE id = :bookId")
    suspend fun updateReadingSettings(bookId: Long, fontSize: Float, isDarkMode: Boolean)
    
    /**
     * Toggle stato preferito
     */
    @Query("UPDATE books SET isFavorite = NOT isFavorite WHERE id = :bookId")
    suspend fun toggleFavorite(bookId: Long)
    
    /**
     * Conta totale libri
     */
    @Query("SELECT COUNT(*) FROM books")
    fun getTotalBooksCount(): Flow<Int>
    
    /**
     * Conta libri completati
     */
    @Query("SELECT COUNT(*) FROM books WHERE isCompleted = 1")
    fun getCompletedBooksCount(): Flow<Int>
}
