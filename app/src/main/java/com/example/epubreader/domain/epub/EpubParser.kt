package com.example.epubreader.domain.epub

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

/**
 * Parser per file EPUB
 * 
 * Usa parsing nativo senza dipendenze esterne.
 * EPUB è essenzialmente un file ZIP con HTML/XHTML all'interno.
 */
class EpubParser(private val context: Context) {
    
    /**
     * Dati estratti da un file EPUB
     */
    data class EpubData(
        val title: String,
        val author: String,
        val coverImagePath: String?,
        val chapters: List<Chapter>,
        val totalCharacters: Long
    )
    
    /**
     * Rappresenta un capitolo del libro
     */
    data class Chapter(
        val index: Int,
        val title: String,
        val content: String,
        val wordCount: Int
    )
    
    /**
     * Parsa un file EPUB e ne estrae i dati
     * 
     * @param uri URI del file EPUB
     * @return Dati estratti dall'EPUB
     */
    suspend fun parseEpub(uri: Uri): EpubData {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open URI: $uri")
        
        var title = "Unknown Title"
        var author = "Unknown Author"
        var coverImagePath: String? = null
        val chapters = mutableListOf<Chapter>()
        val htmlFiles = mutableListOf<Pair<String, String>>() // filename to content
        
        // Estrai file ZIP (EPUB è un ZIP)
        ZipInputStream(inputStream).use { zipStream ->
            var entry = zipStream.nextEntry
            
            while (entry != null) {
                val entryName = entry.name
                
                when {
                    // Metadata (content.opf)
                    entryName.endsWith("content.opf") || entryName.endsWith(".opf") -> {
                        val content = zipStream.readBytes().toString(Charsets.UTF_8)
                        title = extractMetadata(content, "dc:title") ?: title
                        author = extractMetadata(content, "dc:creator") ?: author
                    }
                    
                    // Immagini copertina
                    (entryName.contains("cover", ignoreCase = true) && 
                     (entryName.endsWith(".jpg") || entryName.endsWith(".jpeg") || 
                      entryName.endsWith(".png"))) -> {
                        if (coverImagePath == null) {
                            coverImagePath = saveCoverImage(zipStream.readBytes())
                        }
                    }
                    
                    // File HTML/XHTML (contenuto capitoli)
                    (entryName.endsWith(".html") || entryName.endsWith(".xhtml") || 
                     entryName.endsWith(".htm")) && !entryName.contains("nav") -> {
                        val content = zipStream.readBytes().toString(Charsets.UTF_8)
                        htmlFiles.add(entryName to content)
                    }
                }
                
                zipStream.closeEntry()
                entry = zipStream.nextEntry
            }
        }
        
        inputStream.close()
        
        // Converti HTML files in capitoli
        htmlFiles.sortedBy { it.first }.forEachIndexed { index, (filename, html) ->
            val cleanContent = cleanHtmlContent(html)
            if (cleanContent.length > 100) { // Solo capitoli con contenuto significativo
                val wordCount = cleanContent.split("\\s+".toRegex()).size
                val chapterTitle = extractTitle(html) ?: "Chapter ${index + 1}"
                
                chapters.add(
                    Chapter(
                        index = index,
                        title = chapterTitle,
                        content = cleanContent,
                        wordCount = wordCount
                    )
                )
            }
        }
        
        // Se non ci sono capitoli, crea un capitolo unico con tutto il contenuto
        if (chapters.isEmpty()) {
            val allContent = htmlFiles.joinToString("\n\n") { cleanHtmlContent(it.second) }
            chapters.add(
                Chapter(
                    index = 0,
                    title = "Full Content",
                    content = allContent,
                    wordCount = allContent.split("\\s+".toRegex()).size
                )
            )
        }
        
        val totalCharacters = chapters.sumOf { it.content.length.toLong() }
        
        return EpubData(
            title = title,
            author = author,
            coverImagePath = coverImagePath,
            chapters = chapters,
            totalCharacters = totalCharacters
        )
    }
    
    /**
     * Estrae metadata da content.opf
     */
    private fun extractMetadata(opfContent: String, tag: String): String? {
        val regex = "<$tag[^>]*>([^<]+)</$tag>".toRegex()
        return regex.find(opfContent)?.groupValues?.get(1)?.trim()
    }
    
    /**
     * Estrae il titolo da un file HTML
     */
    private fun extractTitle(html: String): String? {
        val titleRegex = "<title[^>]*>([^<]+)</title>".toRegex(RegexOption.IGNORE_CASE)
        val h1Regex = "<h1[^>]*>([^<]+)</h1>".toRegex(RegexOption.IGNORE_CASE)
        
        return titleRegex.find(html)?.groupValues?.get(1)?.trim()
            ?: h1Regex.find(html)?.groupValues?.get(1)?.trim()
    }
    
    /**
     * Salva l'immagine di copertina
     */
    private fun saveCoverImage(imageData: ByteArray): String? {
        try {
            if (imageData.isEmpty()) return null
            
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                ?: return null
            
            val coverDir = File(context.filesDir, "covers")
            if (!coverDir.exists()) {
                coverDir.mkdirs()
            }
            
            val filename = "cover_${System.currentTimeMillis()}.jpg"
            val coverFile = File(coverDir, filename)
            
            FileOutputStream(coverFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            
            return coverFile.absolutePath
            
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
    /**
     * Pulisce il contenuto HTML rimuovendo tag e formattazione
     */
    private fun cleanHtmlContent(html: String): String {
        return html
            // Rimuovi script e style
            .replace("<script[^>]*>.*?</script>".toRegex(RegexOption.DOT_MATCHES_ALL), "")
            .replace("<style[^>]*>.*?</style>".toRegex(RegexOption.DOT_MATCHES_ALL), "")
            // Converti tag comuni in newline
            .replace("<br\\s*/?>".toRegex(RegexOption.IGNORE_CASE), "\n")
            .replace("<p[^>]*>".toRegex(RegexOption.IGNORE_CASE), "\n")
            .replace("</p>".toRegex(RegexOption.IGNORE_CASE), "\n")
            .replace("<div[^>]*>".toRegex(RegexOption.IGNORE_CASE), "\n")
            .replace("</div>".toRegex(RegexOption.IGNORE_CASE), "\n")
            // Rimuovi tutti gli altri tag HTML
            .replace("<[^>]+>".toRegex(), "")
            // Decodifica HTML entities
            .replace("&nbsp;", " ")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&#8217;", "'")
            .replace("&#8220;", "\"")
            .replace("&#8221;", "\"")
            // Pulisci whitespace
            .replace("\\s+".toRegex(), " ")
            .replace("\n ", "\n")
            .trim()
    }
    
    /**
     * Calcola la percentuale di completamento basata sulla posizione
     * 
     * @param currentChapterIndex Indice del capitolo corrente
     * @param chapterProgress Progresso nel capitolo (0-1)
     * @param totalChapters Numero totale di capitoli
     */
    fun calculateCompletionPercentage(
        currentChapterIndex: Int,
        chapterProgress: Float,
        totalChapters: Int
    ): Float {
        if (totalChapters == 0) return 0f
        
        val completedChapters = currentChapterIndex.toFloat()
        val currentChapterContribution = chapterProgress / totalChapters
        
        val percentage = ((completedChapters + currentChapterContribution) / totalChapters) * 100
        return percentage.coerceIn(0f, 100f)
    }
    
    /**
     * Stima il numero di "pagine" basandosi sul numero di parole
     * (assumendo ~250 parole per pagina)
     */
    fun estimatePageCount(chapters: List<Chapter>): Int {
        val totalWords = chapters.sumOf { it.wordCount }
        return (totalWords / 250).coerceAtLeast(1)
    }
}
