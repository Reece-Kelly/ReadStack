package com.example.assignmentapp.data

import kotlinx.coroutines.flow.Flow

interface BooksRepository {

    // Home Screen: Load books saved in the local database
    fun getBooks(): Flow<List<Volume>>

    // Search Screen: Fetch books from the remote API using a search query
    suspend fun fetchRemoteBooks(query: String)

    // Save a book (e.g., from search results) to the database
    suspend fun saveBook(
        volume: Volume,
        status: BookStatus? = null,
        review: String? = null,
        rating: Float? = null,
        currentPageNumber: Int? = 0,
        totalPageNumber: Int? = 0,
    )

    // Search for books using a search query and return the results
    suspend fun searchBooks(query: String): List<Volume>

    suspend fun getRandomBookFromDb(): BookEntity?

    suspend fun getRandomHighlyRatedBook(minRating: Float): BookEntity?
}
