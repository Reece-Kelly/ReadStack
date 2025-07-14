package com.example.assignmentapp.views

import com.example.assignmentapp.data.Book

data class ReadStackUIState (
    val books: List<Book> = emptyList(), // Uncomment once implemented
    val isLoading: Boolean = false,
    val currentSearchWord: String = "",
    val error: String? = null,
)