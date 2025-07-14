package com.example.assignmentapp.di

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


private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

val appModules = module {
    single<BooksRepository> { BooksRepositoryImpl(get(), get()) }
    single { Dispatchers.IO }
    single { ReadStackViewModel(get()) }
    single {
        Retrofit.Builder()
            .addConverterFactory(
                json.asConverterFactory(contentType = "application/json".toMediaType())
            )
            .baseUrl("https://www.googleapis.com/books/v1/volumes/")
            .build()
    }
    single { get<Retrofit>().create(BooksAPI::class.java) }
}