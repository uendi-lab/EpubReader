package com.example.epubreader.domain.epub

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.Html
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

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
        val htmlFiles = mutableMapOf<String, String>() // filename to content
        var tocFileContent: String? = null
        
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
                     entryName.endsWith(".htm")) -> {
                        val content = zipStream.readBytes().toString(Charsets.UTF_8)
                        htmlFiles[entryName] = content
                    }

                    // Table of Contents
                    entryName.endsWith("toc.ncx") || entryName.endsWith("nav.xhtml") -> {
                        tocFileContent = zipStream.readBytes().toString(Charsets.UTF_8)
                    }
                }
                
                zipStream.closeEntry()
                entry = zipStream.nextEntry
            }
        }
        
        inputStream.close()
        
        // Parse table of contents
        val parsedChapters = tocFileContent?.let { parseTableOfContents(it, htmlFiles) } ?: emptyList()

        if (parsedChapters.isNotEmpty()) {
            chapters.addAll(parsedChapters)
        } else {
            // Fallback for books without a standard TOC
            htmlFiles.entries.sortedBy { it.key }.forEachIndexed { index, (filename, html) ->
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
        }

        // Se non ci sono capitoli, crea un capitolo unico con tutto il contenuto
        if (chapters.isEmpty()) {
            val allContent = htmlFiles.values.joinToString("\n\n") { cleanHtmlContent(it) }
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

    private fun parseTableOfContents(tocContent: String, htmlFiles: Map<String, String>): List<Chapter> {
        val chapters = mutableListOf<Chapter>()
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(tocContent.reader())

            var eventType = parser.eventType
            var currentChapter: Chapter? = null
            var chapterIndex = 0

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (parser.name == "navPoint") {
                            val title = parser.getAttributeValue(null, "playOrder")
                            val contentSrc = parser.getAttributeValue(null, "src")
                            
                            val chapterHtml = htmlFiles[contentSrc]
                            if(chapterHtml != null) {
                                val cleanContent = cleanHtmlContent(chapterHtml)
                                val wordCount = cleanContent.split("\\s+".toRegex()).size
                                 chapters.add(
                                    Chapter(
                                        index = chapterIndex++,
                                        title = title ?: "Chapter ${chapterIndex}",
                                        content = cleanContent,
                                        wordCount = wordCount
                                    )
                                )
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return chapters
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
     * Pulisce il contenuto HTML rimuovendo tag e formattazione non desiderati
     */
    private fun cleanHtmlContent(html: String): String {
        // Usa il parser HTML di Android per gestire la formattazione di base
        val spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        return spanned.toString()
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
