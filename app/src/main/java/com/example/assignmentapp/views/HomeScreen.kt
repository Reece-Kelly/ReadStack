package com.example.assignmentapp.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.assignmentapp.Title
import com.example.assignmentapp.data.BookStatus
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    onBookClicked: (Volume) -> Unit,
    navController: NavController
) {
    val readStackViewModel: ReadStackViewModel = getViewModel()
    val currentUiState by readStackViewModel.readStackUIState.collectAsStateWithLifecycle()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Title(title = "ReadStack")
            }

//            item {
//                Button(onClick = { readStackViewModel.clearDatabase() }) {
//                    Text("Clear Database")
//                }
//            }

            item {
                Text(
                    text = "Currently Reading",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            val currentlyReadingBooks =
                currentUiState.volumes.filter { it.status == BookStatus.READING }

            if (currentUiState.isLoading && currentlyReadingBooks.isEmpty()) {
                item { CircularProgressIndicator() }
            }
            if (currentlyReadingBooks.isNotEmpty()) {
                items(
                    items = currentlyReadingBooks,
                    key = { book -> "reading_${book.id}" }
                ) { volume ->
                    VolumeListItem(
                        volume = volume,
                        onBookClicked = onBookClicked
                    )
                }
            } else if (!currentUiState.isLoading && currentUiState.error == null) {
                item {
                    Text(
                        "No books currently reading yet.",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }


            item {
                Text(
                    text = "Have Read",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
            }
            val haveReadBooks = currentUiState.volumes.filter { it.status == BookStatus.READ }
            if (currentUiState.isLoading && haveReadBooks.isEmpty()) {
                item { CircularProgressIndicator() }
            }
            if (haveReadBooks.isNotEmpty()) {
                items(
                    items = haveReadBooks,
                    key = { book -> "read_${book.id}" }
                ) { volume ->
                    VolumeListItem(
                        volume = volume,
                        onBookClicked = onBookClicked
                    )
                }
            } else if (!currentUiState.isLoading && currentUiState.error == null) {
                item { Text("No books read yet.", modifier = Modifier.padding(vertical = 8.dp)) }
            }


            item {
                Text(
                    text = "Want to Read",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
            }
            val wantToReadBooks =
                currentUiState.volumes.filter { it.status == BookStatus.WANT_TO_READ }
            if (currentUiState.isLoading && wantToReadBooks.isEmpty()) {
                item { CircularProgressIndicator() }
            }
            if (wantToReadBooks.isNotEmpty()) {
                items(
                    items = wantToReadBooks,
                    key = { book -> "want_${book.id}" }
                ) { volume ->
                    VolumeListItem(
                        volume = volume,
                        onBookClicked = onBookClicked
                    )
                }
            } else if (!currentUiState.isLoading && currentUiState.error == null) {
                item {
                    Text(
                        "No books on your want to read list yet.",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            currentUiState.error?.let { errorMsg ->
                if (currentlyReadingBooks.isEmpty() && haveReadBooks.isEmpty() && wantToReadBooks.isEmpty()) {
                    item {
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            item {
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}