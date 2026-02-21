package com.example.epubreader.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.epubreader.presentation.viewmodel.StatsViewModel

/**
 * Schermata Statistiche
 * 
 * Mostra statistiche aggregate di lettura
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel,
    onBackClick: () -> Unit
) {
    val stats by viewModel.aggregatedStats.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Today's reading time
            item {
                StatCard(
                    title = "Today's Reading",
                    value = viewModel.formatDuration(stats.todayReadingTime),
                    icon = Icons.Default.Today,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // This month
            item {
                StatCard(
                    title = "This Month",
                    value = viewModel.formatDuration(stats.monthReadingTime),
                    icon = Icons.Default.CalendarMonth,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            // Total reading time
            item {
                StatCard(
                    title = "Total Reading Time",
                    value = viewModel.formatDuration(stats.totalReadingTime),
                    icon = Icons.Default.Timer,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            
            // Books statistics
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SmallStatCard(
                        title = "Total Books",
                        value = stats.totalBooks.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    SmallStatCard(
                        title = "Completed",
                        value = stats.completedBooks.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Fastest read book
            if (stats.fastestReadBook != null) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Speed, null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Fastest Read",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                stats.fastestReadBook!!.book.title,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Completed in ${viewModel.formatDuration(stats.fastestReadBook!!.stats.totalReadingTimeMillis)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            // Average daily reading
            item {
                StatCard(
                    title = "Average Daily Reading",
                    value = "${stats.averageDailyMinutes} minutes",
                    icon = Icons.Default.TrendingUp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Weekly chart
            if (stats.weeklyReadingData.isNotEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "This Week",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Simple bar chart
                            WeeklyChart(data = stats.weeklyReadingData)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyMedium)
                Text(
                    value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SmallStatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WeeklyChart(data: Map<String, Long>) {
    val maxValue = data.values.maxOrNull() ?: 1L
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.forEach { (day, minutes) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                // Simple bar
                val height = if (maxValue > 0) ((minutes.toFloat() / maxValue) * 100).dp else 0.dp
                
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(height.coerceAtLeast(4.dp))
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    ) {}
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    day,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "${minutes}m",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
