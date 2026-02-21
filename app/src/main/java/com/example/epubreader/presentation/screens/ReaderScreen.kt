package com.example.epubreader.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.epubreader.presentation.viewmodel.ReaderViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

/**
 * Schermata di lettura con tracking automatico
 *
 * Il tracking inizia automaticamente all'apertura e si ferma alla chiusura
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    onBackClick: () -> Unit
) {
    val book by viewModel.book.collectAsState()
    val chapters by viewModel.chapters.collectAsState()
    val currentChapterIndex by viewModel.currentChapterIndex.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()

    var showSettings by remember { mutableStateOf(false) }

    // Gestione lifecycle automatica per pause/resume
    LifecycleResumeEffect {
        viewModel.resumeReading()
        onPauseOrDispose {
            viewModel.pauseReading()
        }
    }

    // Cleanup quando si esce
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopReading()
        }
    }

    val colorScheme = if (isDarkMode) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(book?.title ?: "", style = MaterialTheme.typography.titleMedium)
                            Text(
                                formatElapsedTime(elapsedTime),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showSettings = !showSettings }) {
                            Icon(Icons.Default.Settings, "Settings")
                        }
                    }
                )
            },
            bottomBar = {
                ReaderBottomBar(
                    currentChapter = currentChapterIndex + 1,
                    totalChapters = chapters.size,
                    onPreviousChapter = { viewModel.previousChapter() },
                    onNextChapter = { viewModel.nextChapter() },
                    hasNext = currentChapterIndex < chapters.size - 1,
                    hasPrevious = currentChapterIndex > 0
                )
            }
        ) { padding ->
            BoxWithConstraints(modifier = Modifier.padding(padding)) {
                val currentChapter = chapters.getOrNull(currentChapterIndex)
                if (currentChapter != null) {
                    val textMeasurer = rememberTextMeasurer()
                    val paginatedContent = remember(currentChapter, fontSize, maxWidth, maxHeight) {
                        paginate(
                            text = AnnotatedString(currentChapter.content),
                            textMeasurer = textMeasurer,
                            constraints = Constraints(
                                maxWidth = maxWidth.value.toInt(),
                                maxHeight = maxHeight.value.toInt()
                            ),
                            style = TextStyle(
                                fontSize = fontSize.sp,
                                lineHeight = (fontSize * 1.5).sp,
                                textAlign = TextAlign.Justify
                            )
                        )
                    }

                    if (paginatedContent.isNotEmpty()) {
                        val pagerState = rememberPagerState(pageCount = { paginatedContent.size })

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = currentChapter.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Text(
                                    text = paginatedContent[page]
                                )
                            }
                        }

                        LaunchedEffect(pagerState.currentPage) {
                            val progress = if (paginatedContent.size > 1) {
                                pagerState.currentPage.toFloat() / (paginatedContent.size - 1)
                            } else 0f
                            viewModel.updateChapterProgress(progress)
                        }
                    }
                }

                // Panel impostazioni
                if (showSettings) {
                    ReaderSettingsPanel(
                        fontSize = fontSize,
                        isDarkMode = isDarkMode,
                        onFontSizeIncrease = { viewModel.increaseFontSize() },
                        onFontSizeDecrease = { viewModel.decreaseFontSize() },
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        onDismiss = { showSettings = false }
                    )
                }
            }
        }
    }
}

private fun paginate(
    text: AnnotatedString,
    textMeasurer: TextMeasurer,
    constraints: Constraints,
    style: TextStyle
): List<AnnotatedString> {
    if (text.isEmpty() || constraints.maxWidth <= 0 || constraints.maxHeight <= 0) {
        return emptyList()
    }

    val pages = mutableListOf<AnnotatedString>()
    var currentOffset = 0

    while (currentOffset < text.length) {
        val result = textMeasurer.measure(
            text = text.subSequence(currentOffset, text.length),
            style = style,
            constraints = constraints
        )

        val lastCharIndex = result.layoutInput.text.length - 1
        val didOverflow = result.didOverflowHeight

        val breakIndex = if (didOverflow) {
            result.getLineEnd(result.lineCount - 2, visibleEnd = true)
        } else {
            lastCharIndex
        }

        var adjustedBreakIndex = text.subSequence(currentOffset, currentOffset + breakIndex).lastIndexOf(' ')
        if (adjustedBreakIndex == -1) {
            adjustedBreakIndex = breakIndex
        }

        pages.add(text.subSequence(currentOffset, currentOffset + adjustedBreakIndex))
        currentOffset += adjustedBreakIndex
    }

    return pages
}

@Composable
fun ReaderBottomBar(
    currentChapter: Int,
    totalChapters: Int,
    onPreviousChapter: () -> Unit,
    onNextChapter: () -> Unit,
    hasNext: Boolean,
    hasPrevious: Boolean
) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousChapter,
                enabled = hasPrevious
            ) {
                Icon(Icons.Default.NavigateBefore, "Previous")
            }

            Text("Chapter $currentChapter / $totalChapters")

            IconButton(
                onClick = onNextChapter,
                enabled = hasNext
            ) {
                Icon(Icons.Default.NavigateNext, "Next")
            }
        }
    }
}

@Composable
fun ReaderSettingsPanel(
    fontSize: Float,
    isDarkMode: Boolean,
    onFontSizeIncrease: () -> Unit,
    onFontSizeDecrease: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Reading Settings", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Font size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Font Size")
                Row {
                    IconButton(onClick = onFontSizeDecrease) {
                        Icon(Icons.Default.Remove, "Decrease")
                    }
                    Text("${fontSize.toInt()}", modifier = Modifier.padding(horizontal = 16.dp))
                    IconButton(onClick = onFontSizeIncrease) {
                        Icon(Icons.Default.Add, "Increase")
                    }
                }
            }

            // Dark mode
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Mode")
                Switch(checked = isDarkMode, onCheckedChange = { onToggleDarkMode() })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Close")
            }
        }
    }
}

private fun formatElapsedTime(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = millis / (1000 * 60 * 60)

    return when {
        hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
        minutes > 0 -> String.format("%d:%02d", minutes, seconds)
        else -> String.format("0:%02d", seconds)
    }
}
