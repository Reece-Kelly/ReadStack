package com.example.assignmentapp.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

class BooksTypeConverters {
    @TypeConverter
    fun convertAuthorsToString(authors: List<String>): String {
        return Json.encodeToString(ListSerializer(String.serializer()), authors)
    }

    @TypeConverter
    fun convertStringToAuthors(authorsString: String): List<String> {
        return Json.decodeFromString(ListSerializer(String.serializer()), authorsString)
    }
}