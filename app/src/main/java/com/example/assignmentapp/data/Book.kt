package com.example.assignmentapp.data

data class Book(
    val title: String,
    val author: String,
    val yearPublished: Int,
    val currentPageNumber: Int,
    val totalPageNumber: Int,
    val rating: Double,
    val review: String,
)
