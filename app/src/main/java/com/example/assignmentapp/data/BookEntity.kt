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
    val currentPageNumber: Int,
    val totalPageNumber: Int,
    val rating: Double,
    val review: String,
)
