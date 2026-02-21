package com.example.epubreader.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epubreader.data.local.entity.Book
import com.example.epubreader.data.repository.BookRepository
import com.example.epubreader.data.repository.ReadingStatsRepository
import com.example.epubreader.domain.epub.EpubParser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel per la schermata Libreria
 * 
 * Gestisce la lista dei libri e l'importazione di nuovi EPUB
 */
class LibraryViewModel(
    private val bookRepository: BookRepository,
    private val statsRepository: ReadingStatsRepository,
    private val epubParser: EpubParser
) : ViewModel() {
    
    // Lista di tutti i libri
    val allBooks: StateFlow<List<Book>> = bookRepository.getAllBooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Libri in corso di lettura
    val inProgressBooks: StateFlow<List<Book>> = bookRepository.getInProgressBooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Libri completati
    val completedBooks: StateFlow<List<Book>> = bookRepository.getCompletedBooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Stato UI
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val message: String) : UiState()
        data class Error(val message: String) : UiState()
    }
    
    /**
     * Importa un nuovo libro EPUB
     * 
     * @param uri URI del file EPUB selezionato dall'utente
     */
    fun importBook(uri: Uri) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                // Parsa l'EPUB
                val epubData = epubParser.parseEpub(uri)
                
                // Stima numero di pagine
                val estimatedPages = epubParser.estimatePageCount(epubData.chapters)
                
                // Crea entità Book
                val book = Book(
                    title = epubData.title,
                    author = epubData.author,
                    filePath = uri.toString(),
                    coverImagePath = epubData.coverImagePath,
                    totalPages = estimatedPages,
                    totalCharacters = epubData.totalCharacters,
                    addedDate = System.currentTimeMillis()
                )
                
                // Salva nel database
                val bookId = bookRepository.insertBook(book)
                
                _uiState.value = UiState.Success("Book imported successfully!")
                
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UiState.Error("Failed to import book: ${e.message}")
            }
        }
    }
    
    /**
     * Elimina un libro
     */
    fun deleteBook(book: Book) {
        viewModelScope.launch {
            try {
                bookRepository.deleteBook(book)
                statsRepository.deleteStatsForBook(book.id)
                _uiState.value = UiState.Success("Book deleted")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to delete book: ${e.message}")
            }
        }
    }
    
    /**
     * Toggle preferito
     */
    fun toggleFavorite(bookId: Long) {
        viewModelScope.launch {
            bookRepository.toggleFavorite(bookId)
        }
    }
    
    /**
     * Reset UI state
     */
    fun resetUiState() {
        _uiState.value = UiState.Idle
    }
}
