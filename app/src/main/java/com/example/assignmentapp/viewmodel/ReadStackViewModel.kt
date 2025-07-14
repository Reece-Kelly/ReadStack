package com.example.assignmentapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapp.views.ReadStackUIState
import com.example.assignmentapp.data.BooksRepository
import com.example.assignmentapp.data.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReadStackViewModel(
    private val booksRepository: BooksRepository
): ViewModel() {
    val readStackUIState = MutableStateFlow(ReadStackUIState())

    init {
        getBooks()
    }

    private fun getBooks() {
        readStackUIState.value = ReadStackUIState(isLoading = true)
        viewModelScope.launch {
            when (val result = booksRepository.getBooks()) {
                is NetworkResult.Success -> {
                    readStackUIState.update {
                        it.copy(isLoading = false, books = result.data)
                    }
                }
                is NetworkResult.Error -> {
                    readStackUIState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }
            }
        }
    }
}