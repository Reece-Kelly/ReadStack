package com.example.assignmentapp.data

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val yearPublished: Int,
    val currentPageNumber: Int,
    val totalPageNumber: Int,
    val rating: Double?, // Nullable if not all books have ratings
    val review: String?, // Nullable if not all books have reviews
)
