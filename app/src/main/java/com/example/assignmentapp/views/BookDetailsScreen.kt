package com.example.assignmentapp.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.assignmentapp.data.BookStatus
import com.example.assignmentapp.data.Volume


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookDetailsScreenContent(
    modifier: Modifier = Modifier,
    volume: Volume,
) {
    val volumeInfo = volume.volumeInfo

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        val thumbnailUrl = volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
        if (thumbnailUrl != null) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "${volumeInfo.title} cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Title: ${volumeInfo.title}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Author(s): ${volumeInfo.authors?.joinToString(", ") ?: "Unknown"}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Publisher: ${volumeInfo.publisher.ifEmpty { "Unknown" }}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Published Date: ${volumeInfo.publishedDate ?: "Unknown"}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Description: ${(volumeInfo.description ?: "Unknown").take(300)}...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    volume: Volume,
    onBackPressed: () -> Unit,
    onSaveBook: (BookStatus) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
            ) {
                BookDetailsScreenContent(
                    modifier = Modifier.weight(1f),
                    volume = volume
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onSaveBook(BookStatus.READING) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mark as Reading")
                    }
                    Button(
                        onClick = { onSaveBook(BookStatus.READ) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mark as Read")
                    }
                    Button(
                        onClick = { onSaveBook(BookStatus.WANT_TO_READ) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Want to Read")
                    }
                }
            }
        }
    )
}
