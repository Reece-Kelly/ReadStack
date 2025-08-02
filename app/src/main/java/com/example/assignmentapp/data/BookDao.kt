package com.example.assignmentapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookEntity: BookEntity)

    @Query("SELECT * FROM Book")
    fun getBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM Book WHERE id = :id")
    fun getBookById(id: String): Flow<BookEntity?>

    @Query("SELECT * FROM Book ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomBook(): BookEntity

    @Query("SELECT * FROM Book WHERE rating >= :minRating ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomHighlyRatedBook(minRating: Float): BookEntity?

    @Query("DELETE FROM Book")
    suspend fun clearAll()
}