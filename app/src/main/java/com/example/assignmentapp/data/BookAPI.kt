package com.example.assignmentapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksAPI {
    @GET("books")
    suspend fun fetchBooks(
        @Query("tag") tag: String,
    ): Response<List<Book>>
}