package com.example.epubreader.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epubreader.data.local.entity.Book
import com.example.epubreader.data.local.entity.Bookmark
import com.example.epubreader.data.local.entity.Highlight
import com.example.epubreader.data.repository.BookRepository
import com.example.epubreader.data.repository.ReadingSessionRepository
import com.example.epubreader.data.repository.ReadingStatsRepository
import com.example.epubreader.domain.epub.EpubParser
import com.example.epubreader.domain.tracking.ReadingTracker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel per la schermata di lettura
 * 
 * Gestisce:
 * - Contenuto del libro
 * - Tracking automatico del tempo di lettura
 * - Progresso
 * - Impostazioni di lettura
 * - Segnalibri ed evidenziazioni
 */
class ReaderViewModel(
    private val bookId: Long,
    private val bookRepository: BookRepository,
    private val sessionRepository: ReadingSessionRepository,
    private val statsRepository: ReadingStatsRepository,
    private val epubParser: EpubParser
) : ViewModel() {
    
    // Libro corrente
    val book: StateFlow<Book?> = bookRepository.getBookById(bookId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    // Capitoli del libro
    private val _chapters = MutableStateFlow<List<EpubParser.Chapter>>(emptyList())
    val chapters: StateFlow<List<EpubParser.Chapter>> = _chapters.asStateFlow()
    
    // Capitolo corrente
    private val _currentChapterIndex = MutableStateFlow(0)
    val currentChapterIndex: StateFlow<Int> = _currentChapterIndex.asStateFlow()
    
    // Progresso nel capitolo corrente
    private val _chapterProgress = MutableStateFlow(0f)
    val chapterProgress: StateFlow<Float> = _chapterProgress.asStateFlow()
    
    // Impostazioni di lettura
    private val _fontSize = MutableStateFlow(16f)
    val fontSize: StateFlow<Float> = _fontSize.asStateFlow()
    
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()
    
    // Stato tracking
    val trackingState = ReadingTracker.trackingState
    val elapsedTime = ReadingTracker.elapsedTime
    
    // Stato UI
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    sealed class UiState {
        object Loading : UiState()
        object Ready : UiState()
        data class Error(val message: String) : UiState()
    }
    
    init {
        loadBook()
    }
    
    /**
     * Carica il libro e i suoi contenuti
     */
    private fun loadBook() {
        viewModelScope.launch {
            try {
                val currentBook = bookRepository.getBookByIdSync(bookId)
                if (currentBook == null) {
                    _uiState.value = UiState.Error("Book not found")
                    return@launch
                }
                
                // Carica impostazioni del libro
                _fontSize.value = currentBook.fontSize
                _isDarkMode.value = currentBook.isDarkMode
                _currentChapterIndex.value = currentBook.currentChapterIndex
                _chapterProgress.value = currentBook.currentChapterProgress
                
                // Parsa l'EPUB per ottenere i capitoli
                val epubData = epubParser.parseEpub(android.net.Uri.parse(currentBook.filePath))
                _chapters.value = epubData.chapters
                
                _uiState.value = UiState.Ready
                
                // Avvia il tracking automaticamente
                startReading()
                
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UiState.Error("Failed to load book: ${e.message}")
            }
        }
    }
    
    /**
     * Avvia la sessione di lettura (chiamata automaticamente)
     */
    private fun startReading() {
        viewModelScope.launch {
            // Imposta data di inizio se è la prima volta
            bookRepository.setStartDateIfNull(bookId)
            
            // Avvia tracking automatico
            val currentBook = book.value ?: return@launch
            ReadingTracker.startTracking(bookId, currentBook.currentPosition)
            
            // Crea sessione nel database
            sessionRepository.startSession(bookId, currentBook.currentPosition)
        }
    }
    
    /**
     * Mette in pausa la lettura (chiamata quando app va in background)
     */
    fun pauseReading() {
        ReadingTracker.pauseTracking()
    }
    
    /**
     * Riprende la lettura (chiamata quando app torna in foreground)
     */
    fun resumeReading() {
        ReadingTracker.resumeTracking()
    }
    
    /**
     * Ferma la sessione di lettura (chiamata quando si chiude il libro)
     */
    fun stopReading() {
        viewModelScope.launch {
            // Ferma il tracker e ottieni i dati della sessione
            val sessionData = ReadingTracker.stopTracking()
            
            if (sessionData != null) {
                // Chiudi la sessione nel database
                val currentBook = book.value ?: return@launch
                val pagesRead = calculatePagesRead(sessionData.startPosition, sessionData.currentPosition)
                
                sessionRepository.endSession(
                    bookId = bookId,
                    endPosition = sessionData.currentPosition,
                    pagesRead = pagesRead
                )
                
                // Ricalcola statistiche
                statsRepository.recalculateStatsForBook(bookId, currentBook.completionPercentage)
                statsRepository.updateReadingStreak(bookId)
            }
        }
    }
    
    /**
     * Cambia capitolo
     */
    fun goToChapter(chapterIndex: Int) {
        if (chapterIndex < 0 || chapterIndex >= _chapters.value.size) return
        
        _currentChapterIndex.value = chapterIndex
        _chapterProgress.value = 0f
        
        updateProgress()
    }
    
    /**
     * Capitolo precedente
     */
    fun previousChapter() {
        val current = _currentChapterIndex.value
        if (current > 0) {
            goToChapter(current - 1)
        }
    }
    
    /**
     * Capitolo successivo
     */
    fun nextChapter() {
        val current = _currentChapterIndex.value
        if (current < _chapters.value.size - 1) {
            goToChapter(current + 1)
        }
    }
    
    /**
     * Aggiorna il progresso nel capitolo corrente
     */
    fun updateChapterProgress(progress: Float) {
        _chapterProgress.value = progress.coerceIn(0f, 1f)
        updateProgress()
    }
    
    /**
     * Aggiorna il progresso nel database
     */
    private fun updateProgress() {
        viewModelScope.launch {
            val completionPercentage = epubParser.calculateCompletionPercentage(
                currentChapterIndex = _currentChapterIndex.value,
                chapterProgress = _chapterProgress.value,
                totalChapters = _chapters.value.size
            )
            
            // Calcola posizione assoluta
            val position = calculateAbsolutePosition()
            
            // Aggiorna nel database
            bookRepository.updateReadingProgress(
                bookId = bookId,
                position = position,
                chapterIndex = _currentChapterIndex.value,
                chapterProgress = _chapterProgress.value,
                completionPercentage = completionPercentage
            )
            
            // Aggiorna tracker
            val pagesRead = calculatePagesRead(ReadingTracker.currentSession.value?.startPosition ?: 0, position)
            ReadingTracker.updatePosition(position, pagesRead)
        }
    }
    
    /**
     * Calcola posizione assoluta nel libro
     */
    private fun calculateAbsolutePosition(): Int {
        val chapters = _chapters.value
        if (chapters.isEmpty()) return 0
        
        var position = 0
        for (i in 0 until _currentChapterIndex.value) {
            position += chapters[i].content.length
        }
        
        val currentChapter = chapters.getOrNull(_currentChapterIndex.value)
        if (currentChapter != null) {
            position += (currentChapter.content.length * _chapterProgress.value).toInt()
        }
        
        return position
    }
    
    /**
     * Calcola pagine lette
     */
    private fun calculatePagesRead(startPosition: Int, endPosition: Int): Int {
        val charactersDiff = (endPosition - startPosition).coerceAtLeast(0)
        // Assumiamo ~1500 caratteri per pagina
        return (charactersDiff / 1500).coerceAtLeast(0)
    }
    
    /**
     * Aumenta dimensione font
     */
    fun increaseFontSize() {
        val newSize = (_fontSize.value + 2f).coerceAtMost(32f)
        _fontSize.value = newSize
        saveFontSize(newSize)
    }
    
    /**
     * Diminuisci dimensione font
     */
    fun decreaseFontSize() {
        val newSize = (_fontSize.value - 2f).coerceAtLeast(12f)
        _fontSize.value = newSize
        saveFontSize(newSize)
    }
    
    /**
     * Salva dimensione font
     */
    private fun saveFontSize(size: Float) {
        viewModelScope.launch {
            bookRepository.updateReadingSettings(bookId, size, _isDarkMode.value)
        }
    }
    
    /**
     * Toggle dark mode
     */
    fun toggleDarkMode() {
        val newMode = !_isDarkMode.value
        _isDarkMode.value = newMode
        viewModelScope.launch {
            bookRepository.updateReadingSettings(bookId, _fontSize.value, newMode)
        }
    }
    
    /**
     * Cleanup quando il ViewModel viene distrutto
     */
    override fun onCleared() {
        super.onCleared()
        stopReading()
    }
}
