package com.example.assignmentapp.data

interface BooksRepository {
    suspend fun getBooks(): NetworkResult<List<Book>>
}