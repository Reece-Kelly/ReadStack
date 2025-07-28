package com.example.assignmentapp.views

import com.example.assignmentapp.data.Volume

data class ReadStackUIState(
    val volumes: List<Volume> = emptyList(),
    val searchResults: List<Volume> = emptyList(),
    val isLoading: Boolean = false,
    val currentSearchWord: String = "",
    val error: String? = null
)
