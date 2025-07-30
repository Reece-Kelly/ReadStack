package com.example.assignmentapp.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.assignmentapp.Title
import com.example.assignmentapp.data.BookEntity
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestScreen(
    onBookClicked: (Volume) -> Unit,
    navController: NavController
) {
    val readStackViewModel: ReadStackViewModel = getViewModel()

    val uiState by readStackViewModel.readStackUIState.collectAsState()

    // Find a random book from database and query API using the author of that book
    LaunchedEffect(Unit) {
        val randomBook: BookEntity? = readStackViewModel.getRandomBookFromDb()
        val searchQuery = randomBook?.authors[0]
        readStackViewModel.searchBooks(searchQuery?.trim() ?: "")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(
                navController = navController
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Title(title = "ReadStack")

            Title(title = "Suggest")

            Text(
                text = "Here are some book suggestions based on what you have read!",
                style = MaterialTheme.typography.bodyMedium,
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.searchResults) { volume ->
                        VolumeListItem(volume = volume, onBookClicked = onBookClicked)
                    }
                }
            }

            uiState.error?.let { errorMsg ->
                Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}