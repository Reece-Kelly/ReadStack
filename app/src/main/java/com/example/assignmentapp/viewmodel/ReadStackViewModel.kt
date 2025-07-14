package com.example.assignmentapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.assignmentapp.ReadStackUiState
import com.example.assignmentapp.data.BooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//class ReadStackViewModel: ViewModel() {
//    private val _uiState = MutableStateFlow(ReadStackUiState())
//    val uiState: StateFlow<ReadStackUiState> = _uiState.asStateFlow()
//}

class ReadStackViewModel(
    private val booksRepository: BooksRepository
): ViewModel() {

    fun getBooks() = booksRepository.getBooks()
}