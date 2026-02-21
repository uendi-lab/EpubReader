package com.example.epubreader.data.repository

import com.example.epubreader.data.local.dao.BookDao
import com.example.epubreader.data.local.entity.Book
import kotlinx.coroutines.flow.Flow

/**
 * Repository per operazioni sui libri
 * 
 * Centralizza la logica di accesso ai dati per i libri.
 * Layer intermedio tra ViewModel e Database.
 */
class BookRepository(private val bookDao: BookDao) {
    
    /**
     * Ottiene tutti i libri come Flow (aggiornamento automatico)
     */
    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()
    
    /**
     * Ottiene un libro specifico per ID
     */
    fun getBookById(bookId: Long): Flow<Book?> = bookDao.getBookById(bookId)
    
    /**
     * Ottiene un libro specifico per ID (versione sincrona)
     */
    suspend fun getBookByIdSync(bookId: Long): Book? = bookDao.getBookByIdSync(bookId)
    
    /**
     * Ottiene libri in corso di lettura
     */
    fun getInProgressBooks(): Flow<List<Book>> = bookDao.getInProgressBooks()
    
    /**
     * Ottiene libri completati
     */
    fun getCompletedBooks(): Flow<List<Book>> = bookDao.getCompletedBooks()
    
    /**
     * Ottiene libri preferiti
     */
    fun getFavoriteBooks(): Flow<List<Book>> = bookDao.getFavoriteBooks()
    
    /**
     * Inserisce un nuovo libro
     */
    suspend fun insertBook(book: Book): Long = bookDao.insertBook(book)
    
    /**
     * Aggiorna un libro esistente
     */
    suspend fun updateBook(book: Book) = bookDao.updateBook(book)
    
    /**
     * Elimina un libro
     */
    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)
    
    /**
     * Elimina un libro per ID
     */
    suspend fun deleteBookById(bookId: Long) = bookDao.deleteBookById(bookId)
    
    /**
     * Aggiorna il progresso di lettura
     * 
     * Calcola automaticamente se il libro è completato (>= 95%)
     * e imposta le date di conseguenza
     */
    suspend fun updateReadingProgress(
        bookId: Long,
        position: Int,
        chapterIndex: Int,
        chapterProgress: Float,
        completionPercentage: Float
    ) {
        val isCompleted = completionPercentage >= 95f
        val endDate = if (isCompleted && bookDao.getBookByIdSync(bookId)?.endDate == null) {
            System.currentTimeMillis()
        } else {
            bookDao.getBookByIdSync(bookId)?.endDate
        }
        
        bookDao.updateReadingProgress(
            bookId = bookId,
            position = position,
            chapterIndex = chapterIndex,
            chapterProgress = chapterProgress,
            completionPercentage = completionPercentage,
            lastReadDate = System.currentTimeMillis(),
            isCompleted = isCompleted,
            endDate = endDate
        )
    }
    
    /**
     * Imposta la data di inizio lettura (solo alla prima apertura)
     */
    suspend fun setStartDateIfNull(bookId: Long) {
        bookDao.setStartDateIfNull(bookId, System.currentTimeMillis())
    }
    
    /**
     * Aggiorna le impostazioni di lettura del libro
     */
    suspend fun updateReadingSettings(bookId: Long, fontSize: Float, isDarkMode: Boolean) {
        bookDao.updateReadingSettings(bookId, fontSize, isDarkMode)
    }
    
    /**
     * Toggle stato preferito
     */
    suspend fun toggleFavorite(bookId: Long) = bookDao.toggleFavorite(bookId)
    
    /**
     * Conta totale libri
     */
    fun getTotalBooksCount(): Flow<Int> = bookDao.getTotalBooksCount()
    
    /**
     * Conta libri completati
     */
    fun getCompletedBooksCount(): Flow<Int> = bookDao.getCompletedBooksCount()
}
