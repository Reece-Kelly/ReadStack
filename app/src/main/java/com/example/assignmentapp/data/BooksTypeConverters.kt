package com.example.assignmentapp.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

class BooksTypeConverters {
    @TypeConverter
    fun convertAuthorsToString(authors: List<String>): String =
        Json.encodeToString(authors)

    @TypeConverter
    fun convertStringToAuthors(data: String): List<String> =
        Json.decodeFromString(data)

    @TypeConverter
    fun fromBookStatus(status: BookStatus): String = status.name

    @TypeConverter
    fun toBookStatus(status: String): BookStatus = BookStatus.valueOf(status)
}
