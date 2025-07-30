package com.example.assignmentapp.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.assignmentapp.data.BookStatus
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookDetailsScreenContent(
    modifier: Modifier = Modifier,
    volume: Volume,
    initialReview: String,
    onReviewChange: (String) -> Unit
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
            text = "Description: ${(volumeInfo.description ?: "Unknown").take(200)}...",
            style = MaterialTheme.typography.bodyMedium
        )

        // Code for review and rating
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = initialReview,
            onValueChange = onReviewChange,
            label = { Text("Review this book") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    passedVolume: Volume, // Receives the full Volume object
    onBackPressed: () -> Unit,
    viewModel: ReadStackViewModel = getViewModel()
) {
    // Attempt to load any existing DB data (status, review) for this book ID.
    // This will update currentBookDetails which might overlay or add to passedVolume's info.
    LaunchedEffect(passedVolume.id) {
        viewModel.loadBookDetails(passedVolume.id)
    }

    // This will be the Volume object from the database if it exists (e.g., with persisted review/status)
    // OR null if the book is not in the database yet.
    val bookFromDb by viewModel.currentBookDetails.collectAsStateWithLifecycle()

    // The volume to actually display. Prioritize DB data if available, else use passedVolume.
    // The review and status will come from bookFromDb if it exists.
    // Other info (title, author, etc.) can come from passedVolume initially.
    val displayVolume = remember(passedVolume, bookFromDb) {
        if (bookFromDb != null) {
            // If book exists in DB, use its data, but ensure core info from passedVolume isn't lost
            // if for some reason it's missing in DB version (shouldn't happen with proper saves)
            passedVolume.copy( // Start with passedVolume details
                status = bookFromDb?.status, // Override with DB status
                review = bookFromDb?.review   // Override with DB review
            )
        } else {
            passedVolume
        }
    }

    var reviewTextState by remember(displayVolume.review) {
        mutableStateOf(displayVolume.review ?: "")
    }

    LaunchedEffect(bookFromDb?.review) {
        val currentReviewInDb = bookFromDb?.review ?: ""
        if (reviewTextState != currentReviewInDb) {
            reviewTextState = currentReviewInDb
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(displayVolume.volumeInfo.title.take(20) + if (displayVolume.volumeInfo.title.length > 20) "..." else "") },
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
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            BookDetailsScreenContent(
                modifier = Modifier.weight(1f),
                volume = displayVolume,
                initialReview = reviewTextState,
                onReviewChange = { newText -> reviewTextState = newText }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.saveBook(displayVolume, BookStatus.READING, reviewTextState)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mark as Reading")
                }
                Button(
                    onClick = {
                        viewModel.saveBook(displayVolume, BookStatus.READ, reviewTextState)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mark as Read")
                }
                Button(
                    onClick = {
                        viewModel.saveBook(displayVolume, BookStatus.WANT_TO_READ, reviewTextState)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Want to Read")
                }
                Button(
                    onClick = {
                        viewModel.saveBook(displayVolume, displayVolume.status, reviewTextState)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = reviewTextState != (displayVolume.review ?: "") || bookFromDb == null
                ) {
                    Text(if (bookFromDb != null && bookFromDb?.review == reviewTextState) "Review Saved" else "Save Review")
                }
            }
        }
    }
}
