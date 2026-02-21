package com.example.epubreader.domain.tracking

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Reading Tracker - Sistema di tracking automatico del tempo di lettura
 * 
 * Questo tracker:
 * - Si avvia automaticamente quando l'utente apre un libro
 * - Si mette in pausa quando l'app va in background
 * - Riprende automaticamente quando l'app torna in foreground
 * - Si ferma quando l'utente chiude il libro
 * 
 * NON richiede pressione di pulsanti start/stop da parte dell'utente.
 */
object ReadingTracker {
    
    /**
     * Stato del tracker
     */
    enum class TrackingState {
        IDLE,       // Non in lettura
        ACTIVE,     // Attivamente in lettura
        PAUSED      // In pausa (app in background)
    }
    
    /**
     * Dati della sessione di lettura corrente
     */
    data class SessionData(
        val bookId: Long,
        val startTime: Long,
        val totalActiveTime: Long = 0L,  // Tempo effettivo di lettura (escluse pause)
        val currentPosition: Int = 0,
        val startPosition: Int = 0,
        val pagesRead: Int = 0
    )
    
    // State flows
    private val _trackingState = MutableStateFlow(TrackingState.IDLE)
    val trackingState: StateFlow<TrackingState> = _trackingState.asStateFlow()
    
    private val _currentSession = MutableStateFlow<SessionData?>(null)
    val currentSession: StateFlow<SessionData?> = _currentSession.asStateFlow()
    
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()
    
    // Variabili private per il tracking
    private var lastActiveTime: Long = 0L
    private var pauseStartTime: Long = 0L
    
    /**
     * Avvia il tracking per un libro
     * Chiamata automaticamente quando si apre la schermata di lettura
     */
    fun startTracking(bookId: Long, startPosition: Int) {
        if (_trackingState.value == TrackingState.ACTIVE && 
            _currentSession.value?.bookId == bookId) {
            // Già in tracking per questo libro
            return
        }
        
        val now = System.currentTimeMillis()
        
        _currentSession.value = SessionData(
            bookId = bookId,
            startTime = now,
            startPosition = startPosition,
            currentPosition = startPosition
        )
        
        _trackingState.value = TrackingState.ACTIVE
        lastActiveTime = now
        _elapsedTime.value = 0L
    }
    
    /**
     * Mette in pausa il tracking
     * Chiamata automaticamente quando l'app va in background
     */
    fun pauseTracking() {
        if (_trackingState.value != TrackingState.ACTIVE) return
        
        val session = _currentSession.value ?: return
        val now = System.currentTimeMillis()
        
        // Aggiungi il tempo dall'ultima riattivazione
        val additionalTime = now - lastActiveTime
        
        _currentSession.value = session.copy(
            totalActiveTime = session.totalActiveTime + additionalTime
        )
        
        _trackingState.value = TrackingState.PAUSED
        pauseStartTime = now
        
        // Aggiorna elapsed time
        _elapsedTime.value = session.totalActiveTime + additionalTime
    }
    
    /**
     * Riprende il tracking
     * Chiamata automaticamente quando l'app torna in foreground
     */
    fun resumeTracking() {
        if (_trackingState.value != TrackingState.PAUSED) return
        
        _trackingState.value = TrackingState.ACTIVE
        lastActiveTime = System.currentTimeMillis()
    }
    
    /**
     * Aggiorna la posizione corrente nel libro
     */
    fun updatePosition(position: Int, pagesRead: Int = 0) {
        val session = _currentSession.value ?: return
        
        _currentSession.value = session.copy(
            currentPosition = position,
            pagesRead = pagesRead
        )
    }
    
    /**
     * Ferma il tracking e restituisce i dati della sessione
     * Chiamata automaticamente quando si chiude il libro
     */
    fun stopTracking(): SessionData? {
        val session = _currentSession.value ?: return null
        
        // Se era attivo, aggiungi il tempo finale
        val finalTime = if (_trackingState.value == TrackingState.ACTIVE) {
            val now = System.currentTimeMillis()
            session.totalActiveTime + (now - lastActiveTime)
        } else {
            session.totalActiveTime
        }
        
        val finalSession = session.copy(totalActiveTime = finalTime)
        
        // Reset state
        _trackingState.value = TrackingState.IDLE
        _currentSession.value = null
        _elapsedTime.value = 0L
        lastActiveTime = 0L
        pauseStartTime = 0L
        
        return finalSession
    }
    
    /**
     * Ottiene il tempo totale trascorso (per UI in tempo reale)
     */
    fun getCurrentElapsedTime(): Long {
        val session = _currentSession.value ?: return 0L
        
        return when (_trackingState.value) {
            TrackingState.ACTIVE -> {
                val now = System.currentTimeMillis()
                session.totalActiveTime + (now - lastActiveTime)
            }
            TrackingState.PAUSED -> session.totalActiveTime
            TrackingState.IDLE -> 0L
        }
    }
    
    /**
     * Verifica se è attualmente in tracking
     */
    fun isTracking(): Boolean {
        return _trackingState.value != TrackingState.IDLE
    }
    
    /**
     * Verifica se è in tracking per un libro specifico
     */
    fun isTrackingBook(bookId: Long): Boolean {
        return _currentSession.value?.bookId == bookId && isTracking()
    }
}
