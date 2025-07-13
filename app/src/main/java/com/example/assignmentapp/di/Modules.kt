package com.example.assignmentapp.di

import com.example.assignmentapp.data.BooksRepository
import com.example.assignmentapp.data.BooksRepositoryImpl
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import org.koin.dsl.module

val appModules = module {
    single<BooksRepository> { BooksRepositoryImpl() }
    single { ReadStackViewModel(get()) }
}