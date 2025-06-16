package com.example.assignmentapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReadStackViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ReadStackUiState())
    val uiState: StateFlow<ReadStackUiState> = _uiState.asStateFlow()
}