package com.example.assignmentapp.data

interface BooksRepository {
    fun getBooks(): List<Book>
}