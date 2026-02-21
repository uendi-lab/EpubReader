package com.example.epubreader.data.local.dao

import androidx.room.*
import com.example.epubreader.data.local.entity.Bookmark
import com.example.epubreader.data.local.entity.Highlight
import kotlinx.coroutines.flow.Flow

/**
 * DAO per operazioni su Bookmark
 */
@Dao
interface BookmarkDao {
    
    @Query("SELECT * FROM bookmarks WHERE bookId = :bookId ORDER BY createdDate DESC")
    fun getBookmarksForBook(bookId: Long): Flow<List<Bookmark>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark): Long
    
    @Update
    suspend fun updateBookmark(bookmark: Bookmark)
    
    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)
    
    @Query("DELETE FROM bookmarks WHERE bookId = :bookId")
    suspend fun deleteBookmarksForBook(bookId: Long)
    
    @Query("SELECT COUNT(*) FROM bookmarks WHERE bookId = :bookId")
    fun getBookmarkCountForBook(bookId: Long): Flow<Int>
}

/**
 * DAO per operazioni su Highlight
 */
@Dao
interface HighlightDao {
    
    @Query("SELECT * FROM highlights WHERE bookId = :bookId ORDER BY chapterIndex, startPosition")
    fun getHighlightsForBook(bookId: Long): Flow<List<Highlight>>
    
    @Query("""
        SELECT * FROM highlights 
        WHERE bookId = :bookId AND chapterIndex = :chapterIndex 
        ORDER BY startPosition
    """)
    suspend fun getHighlightsForChapter(bookId: Long, chapterIndex: Int): List<Highlight>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(highlight: Highlight): Long
    
    @Update
    suspend fun updateHighlight(highlight: Highlight)
    
    @Delete
    suspend fun deleteHighlight(highlight: Highlight)
    
    @Query("DELETE FROM highlights WHERE bookId = :bookId")
    suspend fun deleteHighlightsForBook(bookId: Long)
    
    @Query("SELECT COUNT(*) FROM highlights WHERE bookId = :bookId")
    fun getHighlightCountForBook(bookId: Long): Flow<Int>
}
