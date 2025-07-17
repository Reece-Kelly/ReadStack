package com.example.assignmentapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksAPI {
    @GET("volumes")
    suspend fun fetchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 10,
    ): Response<GoogleBooksApiResponse>
}