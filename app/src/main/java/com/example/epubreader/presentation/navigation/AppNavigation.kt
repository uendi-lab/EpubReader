package com.example.epubreader.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.epubreader.EpubReaderApplication
import com.example.epubreader.data.repository.*
import com.example.epubreader.domain.epub.EpubParser
import com.example.epubreader.presentation.screens.*
import com.example.epubreader.presentation.viewmodel.*

/**
 * Navigazione principale dell'app
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as EpubReaderApplication
    
    // Repository instances
    val database = application.database
    val bookRepository = remember { BookRepository(database.bookDao()) }
    val sessionRepository = remember { ReadingSessionRepository(database.readingSessionDao()) }
    val statsRepository = remember { 
        ReadingStatsRepository(database.readingStatsDao(), database.readingSessionDao()) 
    }
    val epubParser = remember { EpubParser(context) }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Library.route
    ) {
        // Schermata Libreria
        composable(Screen.Library.route) {
            val viewModel: LibraryViewModel = viewModel(
                factory = LibraryViewModelFactory(bookRepository, statsRepository, epubParser)
            )
            LibraryScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    navController.navigate(Screen.Reader.createRoute(bookId))
                },
                onStatsClick = {
                    navController.navigate(Screen.Stats.route)
                }
            )
        }
        
        // Schermata Lettura
        composable(
            route = Screen.Reader.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: return@composable
            val viewModel: ReaderViewModel = viewModel(
                factory = ReaderViewModelFactory(
                    bookId, bookRepository, sessionRepository, statsRepository, epubParser
                )
            )
            ReaderScreen(
                viewModel = viewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
        
        // Schermata Statistiche
        composable(Screen.Stats.route) {
            val viewModel: StatsViewModel = viewModel(
                factory = StatsViewModelFactory(bookRepository, sessionRepository, statsRepository)
            )
            StatsScreen(
                viewModel = viewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}

/**
 * Definizione schermate
 */
sealed class Screen(val route: String) {
    object Library : Screen("library")
    object Reader : Screen("reader/{bookId}") {
        fun createRoute(bookId: Long) = "reader/$bookId"
    }
    object Stats : Screen("stats")
}
