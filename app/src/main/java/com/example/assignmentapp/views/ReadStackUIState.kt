package com.example.assignmentapp.views

import com.example.assignmentapp.data.Book
import com.example.assignmentapp.data.GoogleBooksApiResponse
import com.example.assignmentapp.data.Volume

data class ReadStackUIState (
    val books: List<Book> = emptyList(), // Attribute to store the list of books (once converted from volumes from API)
    val volumes: GoogleBooksApiResponse = GoogleBooksApiResponse(emptyList()),
    val volumeList: List<Volume> = emptyList(), // Attribute to store the list of volumes that Google Book API returns
    val isLoading: Boolean = false,
    val currentSearchWord: String = "",
    val error: String? = null, // Attribute to store the error message if any
)