package com.example.assignmentapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Book")
data class BookEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val authors: List<String>,
    val description: String,
    val publisher: String,
    val publishedDate: String,
    val thumbnail: String,
    val smallThumbnail: String,
    val status: BookStatus?,
    val currentPageNumber: Int? = 0,
    val totalPageNumber: Int? = 0,
    val rating: Float? = null,
    val review: String? = null,
)

enum class BookStatus {
    READING,
    READ,
    WANT_TO_READ
}

