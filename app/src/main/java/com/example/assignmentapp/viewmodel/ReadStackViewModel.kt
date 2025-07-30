package com.example.assignmentapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapp.data.BookEntity
import com.example.assignmentapp.data.BookStatus
import com.example.assignmentapp.data.BooksRepository
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.views.ReadStackUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReadStackViewModel(
    private val booksRepository: BooksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReadStackUIState())
    val readStackUIState: StateFlow<ReadStackUIState> = _uiState

    private val _currentBookDetails = MutableStateFlow<Volume?>(null)
    val currentBookDetails: StateFlow<Volume?> = _currentBookDetails

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

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val results = booksRepository.searchBooks(query)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    searchResults = results,
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


    fun loadBookDetails(bookId: String) {
        viewModelScope.launch {
            Log.d("ViewModel", "loadBookDetails called for ID: $bookId")
            booksRepository.getBooks().firstOrNull()?.find { it.id == bookId }
                ?.let { volumeWithDetails ->
                    Log.d(
                        "ViewModel",
                        "Book found: ${volumeWithDetails.volumeInfo.title}, Rating: ${volumeWithDetails.rating}, Page: ${volumeWithDetails.currentPageNumber}, Total Pages: ${volumeWithDetails.totalPageNumber}"
                    )
                    _currentBookDetails.value = volumeWithDetails
                } ?: run {
                Log.w("ViewModel", "Book with ID $bookId NOT FOUND")
                _currentBookDetails.value = null
            }
        }
    }


    fun saveBook(
        volume: Volume,
        status: BookStatus?,
        review: String?,
        rating: Float?,
        currentPage: Int?,
        totalPageNumber: Int?
    ) {
        viewModelScope.launch {
            try {
                val volumeToSave = volume.copy(
                    review = review,
                    rating = rating,
                    currentPageNumber = currentPage,
                    totalPageNumber = totalPageNumber
                )

                booksRepository.saveBook(
                    volumeToSave,
                    status ?: volumeToSave.status,
                    volumeToSave.review,
                    volumeToSave.rating,
                    volumeToSave.currentPageNumber,
                    volumeToSave.totalPageNumber
                )
                Log.d(
                    "ViewModel",
                    "Book ${volume.id} save initiated. Status: $status, Review: $review, Rating: $rating, Page: $currentPage, Total Pages: $totalPageNumber"
                )
                loadBookDetails(volume.id)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error saving book ${volume.id}: ${e.message}")
                _uiState.value = _uiState.value.copy(error = "Failed to save book: ${e.message}")
            }
        }
    }

    suspend fun getRandomBookFromDb(): BookEntity? {
        viewModelScope.launch {
            try {
                return@launch withContext(Dispatchers.IO) {
                    booksRepository.getRandomBookFromDb()
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error getting random book: ${e.message}")
                _uiState.value = _uiState.value.copy(error = "Failed to get random book: ${e.message}")
            }
        }
        return booksRepository.getRandomBookFromDb()
    }
}