package com.example.assignmentapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapp.data.BookStatus
import com.example.assignmentapp.data.BooksRepository
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.views.ReadStackUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReadStackViewModel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReadStackUIState())
    val readStackUIState: StateFlow<ReadStackUIState> = _uiState

    init {
        observeBooksInDb()
    }

    private fun observeBooksInDb() {
        viewModelScope.launch {
            booksRepository.getBooks().collectLatest { volumes ->
                _uiState.value = ReadStackUIState(
                    isLoading = false,
                    volumes = volumes,
                    error = null
                )
            }
        }
    }

    fun fetchRemoteBooks(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                booksRepository.fetchRemoteBooks(query)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val results = booksRepository.searchBooks(query)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    volumes = results,
                    currentSearchWord = query
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun saveBook(volume: Volume, status: BookStatus) {
        viewModelScope.launch {
            booksRepository.saveBook(volume, status)
        }
    }
}