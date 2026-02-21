# Epub Reader - Android App con Tracking Automatico

Un'app Android nativa completa per la lettura di eBook EPUB con sistema di **tracking automatico** del tempo di lettura, statistiche avanzate e libreria personale.

## 📚 Caratteristiche Principali

### 1. **eBook Reader EPUB**
- Importazione file EPUB dalla memoria del dispositivo
- Libreria con copertine, titoli e autori
- Lettura fluida con navigazione capitoli
- Salvataggio automatico della posizione di lettura
- Impostazioni personalizzabili:
  - Dimensione font regolabile
  - Tema chiaro/scuro
  - Segnalibri (roadmap)
  - Evidenziazioni (roadmap)

### 2. **Tracking Automatico della Lettura** ⏱️

Il sistema di tracking è **completamente automatico**:

- ✅ **Avvio automatico**: Inizia quando apri un libro
- ✅ **Pausa automatica**: Si mette in pausa quando l'app va in background
- ✅ **Resume automatico**: Riprende quando torni nell'app
- ✅ **Stop automatico**: Si ferma quando chiudi il libro

**NON** richiede pressione di pulsanti start/stop!

#### Dati tracciati automaticamente:
- Data e ora di primo avvio → **Data inizio lettura**
- Data e ora di completamento (≥95%) → **Data fine lettura**
- Tempo totale di lettura (solo tempo attivo, escluse pause)
- Numero totale di sessioni
- Durata media delle sessioni
- Velocità media di lettura (pagine/minuto)
- Giorni impiegati per completare il libro

### 3. **Dashboard Statistiche** 📊

Visualizza:
- ⏰ Tempo totale letto oggi
- 📅 Tempo totale letto questo mese
- 🚀 Libro letto più velocemente
- 📈 Media minuti al giorno
- 📊 Grafico andamento lettura settimanale
- 📚 Totale libri e libri completati

### 4. **Architettura**

```
MVVM Architecture
├── UI Layer (Jetpack Compose)
│   ├── LibraryScreen - Libreria libri
│   ├── ReaderScreen - Lettura con tracking automatico
│   └── StatsScreen - Statistiche
├── ViewModel Layer
│   ├── LibraryViewModel
│   ├── ReaderViewModel (gestisce tracking)
│   └── StatsViewModel
├── Domain Layer
│   ├── EpubParser - Parsing file EPUB
│   └── ReadingTracker - Sistema tracking automatico
└── Data Layer (Repository + Room)
    ├── Entities: Book, ReadingSession, ReadingStats, Bookmark, Highlight
    ├── DAOs: BookDao, SessionDao, StatsDao, etc.
    └── Database: Room SQLite
```

### 5. **Database Room** 🗄️

**Entità principali:**

**Book**
- Informazioni libro (titolo, autore, copertina)
- Progresso lettura (posizione, percentuale)
- Date (aggiunta, inizio, fine, ultima lettura)
- Impostazioni personalizzate (font, tema)

**ReadingSession**
- Sessione singola di lettura
- Timestamp inizio/fine
- Durata effettiva (escluse pause)
- Pagine lette

**ReadingStats**
- Statistiche aggregate per libro
- Tempo totale, numero sessioni
- Velocità media (pagine/min, parole/min)
- Streak giorni consecutivi

**Bookmark & Highlight**
- Segnalibri con note
- Evidenziazioni colorate

## 🛠️ Tecnologie Utilizzate

- **Linguaggio**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architettura**: MVVM
- **Database**: Room
- **Async**: Kotlin Coroutines + Flow
- **Navigation**: Navigation Compose
- **EPUB Parser**: epublib-core
- **Image Loading**: Coil
- **Dependency Injection**: Manual (Repository pattern)

## 📋 Requisiti

- Android Studio Hedgehog | 2023.1.1 o superiore
- JDK 17
- Android SDK 34
- Dispositivo/Emulatore Android con API 26+ (Android 8.0+)

## 🚀 Come Compilare e Testare

### 1. Apri il Progetto

```bash
# Clona o copia la cartella del progetto
cd EpubReader

# Apri con Android Studio
# File > Open > Seleziona cartella EpubReader
```

### 2. Sync Gradle

Android Studio sincronizzerà automaticamente le dipendenze. Se non lo fa:
```
File > Sync Project with Gradle Files
```

### 3. Compila l'App

**Da Android Studio:**
```
Build > Make Project (Ctrl+F9 / Cmd+F9)
```

**Da Command Line:**
```bash
# Windows
gradlew assembleDebug

# Linux/Mac
./gradlew assembleDebug
```

L'APK sarà in: `app/build/outputs/apk/debug/app-debug.apk`

### 4. Esegui su Emulatore

1. Crea un emulatore (AVD):
   - Tools > Device Manager > Create Device
   - Scegli Pixel 5 o simile
   - API Level 34 (Android 14)

2. Avvia l'emulatore

3. Run > Run 'app' (Shift+F10)

### 5. Esegui su Dispositivo Fisico

1. Abilita "Opzioni sviluppatore" sul dispositivo:
   - Settings > About phone > Tocca 7 volte su "Build number"
   
2. Abilita "Debug USB":
   - Settings > Developer options > USB debugging

3. Collega il dispositivo via USB

4. Run > Run 'app' (Shift+F10)

## 📖 Come Usare l'App

### Importare un Libro

1. Apri l'app
2. Tocca il pulsante **+ (FAB)** in basso a destra
3. Seleziona un file EPUB dalla memoria
4. Il libro verrà importato automaticamente nella libreria

### Leggere un Libro

1. Dalla libreria, tocca un libro
2. Il **tracking inizia automaticamente** ⏱️
3. Naviga tra i capitoli con le frecce in basso
4. Regola font e tema dal pulsante impostazioni (⚙️)
5. Quando esci, il progresso viene salvato automaticamente

**Il tracking si gestisce da solo:**
- Si avvia quando apri il libro
- Va in pausa se ricevi una chiamata o cambi app
- Riprende quando torni
- Si ferma quando esci dalla lettura

### Visualizzare Statistiche

1. Dalla libreria, tocca l'icona grafico (📊) in alto
2. Visualizza tutte le tue statistiche di lettura
3. Vedi il grafico dell'ultima settimana
4. Scorri per vedere libri completati e velocità di lettura

## 🧪 Test

### Testare il Tracking Automatico

1. Importa un libro di test
2. Apri il libro → Il timer parte
3. Premi Home → App va in background → Timer in pausa
4. Riapri l'app → Timer riprende
5. Torna alla libreria → Sessione salvata automaticamente
6. Controlla le statistiche per vedere il tempo registrato

### Generare Dati di Test

Per testare le statistiche, puoi:
1. Aprire e chiudere un libro più volte (crea sessioni)
2. Navigare tra i capitoli (aumenta progresso)
3. Lasciare l'app aperta per alcuni minuti (accumula tempo)

### Verificare Database

Puoi ispezionare il database Room con:
```
View > Tool Windows > App Inspection > Database Inspector
```

## 📁 Struttura del Progetto

```
app/src/main/java/com/example/epubreader/
├── data/
│   ├── local/
│   │   ├── entity/          # Entità Room (Book, Session, Stats...)
│   │   ├── dao/             # Data Access Objects
│   │   └── EpubReaderDatabase.kt
│   └── repository/          # Repository layer (Book, Session, Stats)
├── domain/
│   ├── epub/                # EpubParser
│   └── tracking/            # ReadingTracker (tracking automatico)
├── presentation/
│   ├── screens/             # UI Compose (Library, Reader, Stats)
│   ├── viewmodel/           # ViewModels + Factories
│   ├── navigation/          # Navigation Compose
│   └── theme/               # Material Theme
├── MainActivity.kt
└── EpubReaderApplication.kt
```

## 🔧 Configurazione Gradle

Il progetto usa:
- Kotlin 1.9.20
- Compose BOM 2023.10.01
- Room 2.6.1
- Navigation Compose 2.7.6
- Coroutines 1.7.3

Tutte le dipendenze sono già configurate in `app/build.gradle.kts`.

## 🐛 Risoluzione Problemi

### Gradle Sync Failed
```bash
# Pulisci e ricostruisci
./gradlew clean
./gradlew build
```

### Errori di Compilazione Room
```bash
# Assicurati di avere KSP configurato
# Verifica in build.gradle.kts:
id("com.google.devtools.ksp") version "1.9.20-1.0.14"
```

### EPUB Non Viene Importato
- Verifica che il file sia un EPUB valido
- Controlla i permessi di storage nel Manifest
- Verifica i log con Logcat

### Il Tracking Non Funziona
- Verifica che il LifecycleObserver stia gestendo pause/resume
- Controlla il database per vedere se le sessioni vengono salvate
- Usa i log per debuggare il ReadingTracker

## 📝 Note Tecniche

### Gestione Lifecycle
L'app usa `LifecycleResumeEffect` per gestire automaticamente:
- Resume: `viewModel.resumeReading()`
- Pause: `viewModel.pauseReading()`
- Dispose: `viewModel.stopReading()`

### Persistenza URI
Gli URI dei file EPUB vengono persistiti usando Storage Access Framework (SAF), garantendo accesso permanente ai file.

### Calcolo Progresso
Il progresso viene calcolato in base a:
- Capitolo corrente / Totale capitoli
- Scroll position nel capitolo
- Percentuale: `(chaptersCompleted + currentChapterProgress) / totalChapters * 100`

### Statistiche in Tempo Reale
Le statistiche vengono ricalcolate automaticamente dopo ogni sessione usando:
```kotlin
statsRepository.recalculateStatsForBook(bookId, completionPercentage)
```

## 🚧 Roadmap / Funzionalità Future

- [ ] Implementazione completa segnalibri
- [ ] Implementazione completa evidenziazioni
- [ ] Notifiche promemoria lettura
- [ ] Sincronizzazione Firebase
- [ ] Esportazione statistiche CSV
- [ ] Temi personalizzabili aggiuntivi
- [ ] Supporto altri formati (PDF, MOBI)
- [ ] Widget home screen
- [ ] Integrazione TTS (Text-to-Speech)

## 📄 Licenza

Questo progetto è stato creato per scopi didattici e dimostrativi.

## 👨‍💻 Autore

Creato con ❤️ usando Android Studio, Kotlin e Jetpack Compose.

---

**Buona lettura! 📚**
