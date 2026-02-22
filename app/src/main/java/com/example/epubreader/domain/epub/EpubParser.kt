package com.example.epubreader.domain.epub

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import nl.siegmann.epublib.epub.EpubReader
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream

/**
 * Parser per file EPUB
 * 
 * Usa la libreria epublib per un parsing robusto e Jsoup per la pulizia dell'HTML.
 */
class EpubParser(private val context: Context) {

    data class EpubData(
        val title: String,
        val author: String,
        val coverImagePath: String?,
        val chapters: List<Chapter>,
        val totalCharacters: Long
    )

    data class Chapter(
        val index: Int,
        val title: String,
        val content: String,
        val wordCount: Int
    )

    suspend fun parseEpub(uri: Uri): EpubData {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open URI: $uri")

        val book = EpubReader().readEpub(inputStream)

        val title = book.metadata.firstTitle ?: "Unknown Title"
        val author = book.metadata.authors.firstOrNull()?.let { "${it.firstname} ${it.lastname}".trim() } ?: "Unknown Author"

        val coverImagePath = book.coverImage?.let {
            saveCoverImage(it.data)
        }

        val chapters = mutableListOf<Chapter>()
        book.tableOfContents.tocReferences.forEachIndexed { index, tocReference ->
            val chapterContentHtml = tocReference.resource.reader.readText()
            val cleanContent = cleanHtmlContent(chapterContentHtml)
            val wordCount = cleanContent.split("\\s+".toRegex()).size
            chapters.add(
                Chapter(
                    index = index,
                    title = tocReference.title ?: "Chapter ${index + 1}",
                    content = cleanContent,
                    wordCount = wordCount
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

    private fun saveCoverImage(imageData: ByteArray): String? {
        return try {
            if (imageData.isEmpty()) return null

            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size) ?: return null

            val coverDir = File(context.filesDir, "covers")
            if (!coverDir.exists()) {
                coverDir.mkdirs()
            }

            val filename = "cover_${System.currentTimeMillis()}.jpg"
            val coverFile = File(coverDir, filename)

            FileOutputStream(coverFile).use { out ->
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
            }

            coverFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun cleanHtmlContent(html: String): String {
        val document = Jsoup.parse(html)
        document.outputSettings().prettyPrint(false)
        document.select("br").after("\n")
        document.select("p").after("\n\n")
        val text = document.wholeText().trim()
        return text.replace("\n ", "\n")
    }

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

    fun estimatePageCount(chapters: List<Chapter>): Int {
        val totalWords = chapters.sumOf { it.wordCount }
        return (totalWords / 250).coerceAtLeast(1)
    }
}
