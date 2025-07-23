package com.example.assignmentapp.di

import androidx.room.Room
import com.example.assignmentapp.data.Book
import com.example.assignmentapp.data.BookDatabase
import com.example.assignmentapp.data.BooksRepository
import com.example.assignmentapp.data.BooksRepositoryImpl
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import com.example.assignmentapp.data.BooksAPI
import org.koin.dsl.module
import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel



private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

val appModules = module {
    single<BooksRepository> { BooksRepositoryImpl(get(), get()) }
    single { Dispatchers.IO }
//    single { ReadStackViewModel(get()) }
    viewModel { ReadStackViewModel(get()) }
    single {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .addConverterFactory(
                json.asConverterFactory(contentType = "application/json".toMediaType())
            )
            .baseUrl("https://www.googleapis.com/books/v1/")
            .build()
    }
    single {
        get<Retrofit>().create(BooksAPI::class.java)
    }

    single {
        Room.databaseBuilder (
            androidContext(),
            BookDatabase::class.java,
            "book_database"
        ).build()
    }
    single { get<BookDatabase>().bookDao() }
}