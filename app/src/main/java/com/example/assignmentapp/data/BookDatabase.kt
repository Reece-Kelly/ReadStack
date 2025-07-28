package com.example.assignmentapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [BookEntity::class],
    version = 2,
)

@TypeConverters(BooksTypeConverters::class)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Book ADD COLUMN status TEXT")
        database.execSQL("ALTER TABLE Book ADD COLUMN currentPageNumber INTEGER")
        database.execSQL("ALTER TABLE Book ADD COLUMN totalPageNumber INTEGER")
        database.execSQL("ALTER TABLE Book ADD COLUMN rating REAL")
        database.execSQL("ALTER TABLE Book ADD COLUMN review TEXT")
    }
}

