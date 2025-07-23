package com.example.assignmentapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database (
    entities =  [BookEntity::class],
    version = 1,
)

@TypeConverters(BooksTypeConverters::class)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
}