package com.example.epubreader.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.epubreader.data.repository.*
import com.example.epubreader.domain.epub.EpubParser

/**
 * Factory per LibraryViewModel
 */
class LibraryViewModelFactory(
    private val bookRepository: BookRepository,
    private val statsRepository: ReadingStatsRepository,
    private val epubParser: EpubParser
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            return LibraryViewModel(bookRepository, statsRepository, epubParser) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory per ReaderViewModel
 */
class ReaderViewModelFactory(
    private val bookId: Long,
    private val bookRepository: BookRepository,
    private val sessionRepository: ReadingSessionRepository,
    private val statsRepository: ReadingStatsRepository,
    private val epubParser: EpubParser
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReaderViewModel::class.java)) {
            return ReaderViewModel(bookId, bookRepository, sessionRepository, statsRepository, epubParser) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory per StatsViewModel
 */
class StatsViewModelFactory(
    private val bookRepository: BookRepository,
    private val sessionRepository: ReadingSessionRepository,
    private val statsRepository: ReadingStatsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            return StatsViewModel(bookRepository, sessionRepository, statsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
