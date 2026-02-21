# Guida Tecnica Approfondita - Epub Reader

## Sistema di Tracking Automatico

### Come Funziona

Il sistema di tracking è basato su tre componenti principali:

#### 1. ReadingTracker (Domain Layer)
```kotlin
class ReadingTracker {
    // Stati possibili
    enum class TrackingState {
        IDLE,       // Non in lettura
        ACTIVE,     // Attivamente in lettura
        PAUSED      // In pausa (app in background)
    }
}
```

**Flusso di tracking:**
1. User apre libro → `startTracking(bookId, position)`
2. App va in background → `pauseTracking()` (automatico)
3. App torna in foreground → `resumeTracking()` (automatico)
4. User chiude libro → `stopTracking()` restituisce SessionData

**Vantaggi:**
- Tracciamento preciso del tempo effettivo
- Esclude automaticamente pause e interruzioni
- Nessuna azione richiesta dall'utente

#### 2. LifecycleObserver in ReaderScreen
```kotlin
LifecycleResumeEffect {
    viewModel.resumeReading()
    onPauseOrDispose {
        viewModel.pauseReading()
    }
}
```

Questo composable si collega automaticamente al lifecycle dell'Activity e gestisce:
- ON_RESUME → Resume tracking
- ON_PAUSE → Pause tracking
- ON_DESTROY → Stop tracking

#### 3. Repository Layer
```kotlin
suspend fun startSession(bookId: Long, startPosition: Int): Long
suspend fun endSession(bookId: Long, endPosition: Int, pagesRead: Int)
```

Crea e chiude le sessioni nel database, calcolando:
- Durata effettiva
- Pagine lette
- Posizioni start/end

### Calcolo Statistiche

Le statistiche vengono ricalcolate automaticamente dopo ogni sessione:

```kotlin
suspend fun recalculateStatsForBook(bookId: Long, completionPercentage: Float) {
    val sessions = sessionDao.getSessionsForBookSync(bookId)
    
    // Calcoli
    val totalTime = sessions.sumOf { it.durationMillis }
    val totalSessions = sessions.size
    val avgDuration = totalTime / totalSessions
    val avgPagesPerMinute = totalPagesRead / (totalTime / 60000.0)
    
    // Salva nel database
    statsDao.insertStats(stats)
}
```

**Metriche calcolate:**
- Tempo totale di lettura
- Numero di sessioni
- Durata media sessioni
- Velocità lettura (pagine/min e parole/min)
- Giorni per completamento (se >= 95%)
- Sessione più lunga/breve
- Streak giorni consecutivi

## Parsing EPUB

### Libreria Utilizzata
`nl.siegmann.epublib:epublib-core:4.0`

### Processo di Parsing

```kotlin
suspend fun parseEpub(uri: Uri): EpubData {
    // 1. Apri stream
    val inputStream = context.contentResolver.openInputStream(uri)
    
    // 2. Leggi EPUB
    val epubBook = epubReader.readEpub(inputStream)
    
    // 3. Estrai metadati
    val title = epubBook.title ?: "Unknown"
    val author = epubBook.metadata?.authors?.firstOrNull()
    
    // 4. Estrai copertina
    val coverImagePath = extractCoverImage(epubBook)
    
    // 5. Estrai capitoli
    val chapters = extractChapters(epubBook)
    
    return EpubData(title, author, coverImagePath, chapters, totalChars)
}
```

### Estrazione Capitoli

```kotlin
epubBook.spine.spineReferences.forEachIndexed { index, spineRef ->
    val resource = spineRef.resource
    val content = String(resource.data, Charsets.UTF_8)
    val cleanContent = cleanHtmlContent(content)
    
    chapters.add(Chapter(
        index = index,
        title = resource.title ?: "Chapter ${index + 1}",
        content = cleanContent,
        wordCount = cleanContent.split("\\s+".toRegex()).size
    ))
}
```

### Pulizia HTML

Il contenuto EPUB è in HTML/XHTML. Viene pulito per la visualizzazione:

```kotlin
private fun cleanHtmlContent(html: String): String {
    return html
        .replace("<br\\s*/?>".toRegex(), "\n")
        .replace("<p[^>]*>".toRegex(), "\n")
        .replace("</p>".toRegex(), "\n")
        .replace("<[^>]+>".toRegex(), "")  // Rimuovi tutti i tag
        .replace("&nbsp;", " ")
        .replace("&quot;", "\"")
        // ... altri entity HTML
        .trim()
}
```

## Calcolo Progresso

### Formula Percentuale Completamento

```kotlin
fun calculateCompletionPercentage(
    currentChapterIndex: Int,
    chapterProgress: Float,  // 0.0 - 1.0
    totalChapters: Int
): Float {
    if (totalChapters == 0) return 0f
    
    val completedChapters = currentChapterIndex.toFloat()
    val currentChapterContribution = chapterProgress / totalChapters
    
    val percentage = ((completedChapters + currentChapterContribution) / totalChapters) * 100
    return percentage.coerceIn(0f, 100f)
}
```

**Esempio:**
- Libro con 10 capitoli
- Attualmente al capitolo 5 (index 4)
- Al 50% del capitolo 5
- Progresso: `(4 + 0.5) / 10 * 100 = 45%`

### Tracking Progresso in ReaderScreen

```kotlin
LaunchedEffect(scrollState.value) {
    val progress = if (scrollState.maxValue > 0) {
        scrollState.value.toFloat() / scrollState.maxValue
    } else 0f
    
    viewModel.updateChapterProgress(progress)
}
```

Ogni volta che l'utente scrolla, il progresso viene aggiornato automaticamente.

## Database Room - Query Avanzate

### Query con Aggregazioni

```kotlin
@Query("""
    SELECT SUM(durationMillis) 
    FROM reading_sessions 
    WHERE sessionStart >= :todayStart AND isActive = 0
""")
suspend fun getTodayReadingTime(todayStart: Long): Long?
```

### Query con Foreign Keys

```kotlin
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE  // Elimina sessioni quando elimini libro
        )
    ],
    indices = [Index("bookId")]  // Indice per performance
)
data class ReadingSession(...)
```

### Flow per UI Reattiva

```kotlin
@Query("SELECT * FROM books ORDER BY lastReadDate DESC")
fun getAllBooks(): Flow<List<Book>>
```

Il `Flow` emette automaticamente nuovi valori quando il database cambia, aggiornando l'UI in tempo reale.

## Gestione Permessi Storage

### Manifest

```xml
<!-- Lettura file (pre-Android 13) -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
```

### Storage Access Framework (SAF)

L'app usa SAF per accedere ai file:

```kotlin
val filePicker = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument()
) { uri: Uri? ->
    uri?.let { 
        // Ottieni permesso persistente
        context.contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        viewModel.importBook(it) 
    }
}

// Lancia picker
filePicker.launch(arrayOf("application/epub+zip"))
```

**Vantaggi SAF:**
- Non richiede permessi storage invasivi
- Accesso sicuro e scoped
- Permessi persistenti agli URI

## Performance e Ottimizzazioni

### 1. Lazy Loading

```kotlin
LazyVerticalGrid(columns = GridCells.Fixed(2)) {
    items(books) { book ->
        BookItem(book = book, onClick = { ... })
    }
}
```

Carica solo gli item visibili, ottimo per librerie grandi.

### 2. Coroutines per I/O

```kotlin
viewModelScope.launch {
    // Operazioni database su IO dispatcher
    withContext(Dispatchers.IO) {
        bookRepository.updateBook(book)
    }
}
```

### 3. StateFlow invece di LiveData

```kotlin
val books: StateFlow<List<Book>> = bookRepository.getAllBooks()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
```

**Vantaggi:**
- Più performante con Compose
- Supporto nativo Kotlin
- Timeout automatico quando nessuno osserva

### 4. Immagini con Coil

```kotlin
AsyncImage(
    model = book.coverImagePath,
    contentDescription = book.title,
    modifier = Modifier.size(200.dp),
    contentScale = ContentScale.Crop
)
```

Coil gestisce automaticamente:
- Caching in memoria e disco
- Loading asincrono
- Placeholder/error handling

## Testing

### Unit Test Example

```kotlin
@Test
fun `calculateCompletionPercentage returns correct value`() {
    val parser = EpubParser(context)
    
    val percentage = parser.calculateCompletionPercentage(
        currentChapterIndex = 4,
        chapterProgress = 0.5f,
        totalChapters = 10
    )
    
    assertEquals(45f, percentage, 0.1f)
}
```

### Room Database Test

```kotlin
@Test
fun `insertBook and getBook returns same book`() = runTest {
    val book = Book(title = "Test", author = "Author", filePath = "path")
    
    val bookId = database.bookDao().insertBook(book)
    val retrieved = database.bookDao().getBookByIdSync(bookId)
    
    assertEquals(book.title, retrieved?.title)
}
```

## Estensioni Future

### Sincronizzazione Firebase

Struttura database già predisposta per sync:

```kotlin
data class Book(
    val id: Long = 0,
    val firebaseId: String? = null,  // Aggiungere
    val lastSyncedAt: Long? = null,  // Aggiungere
    // ... resto campi
)
```

### Notifiche Reading Reminder

```kotlin
// WorkManager per notifiche programmate
class ReadingReminderWorker(context: Context, params: WorkerParameters) 
    : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        // Controlla se l'utente ha letto oggi
        // Se no, mostra notifica
        return Result.success()
    }
}
```

### Export Statistiche CSV

```kotlin
suspend fun exportStatsToCSV(): File {
    val stats = statsRepository.getAllStats().first()
    val csv = StringBuilder()
    csv.append("Book,Total Time,Sessions,Avg Pages/Min\n")
    
    stats.forEach { stat ->
        val book = bookRepository.getBookByIdSync(stat.bookId)
        csv.append("${book?.title},${stat.totalReadingTimeMillis},...")
    }
    
    val file = File(context.getExternalFilesDir(null), "stats.csv")
    file.writeText(csv.toString())
    return file
}
```

## Best Practices Implementate

✅ **Single Source of Truth**: Room database è l'unica fonte di verità
✅ **Separation of Concerns**: Layers ben separati (UI, ViewModel, Repository, Database)
✅ **Reactive UI**: Flow/StateFlow per aggiornamenti automatici
✅ **Lifecycle Awareness**: Gestione corretta di lifecycle per tracking
✅ **Error Handling**: Try-catch su operazioni I/O
✅ **Immutability**: Data classes immutabili
✅ **Coroutines**: Async operations senza blocking main thread
✅ **Material Design 3**: UI moderna e coerente
✅ **Type Safety**: Kotlin types e null safety

---

Per domande o approfondimenti, consulta il codice sorgente commentato!
