package com.example.assignmentapp.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
    initialRating: Float,
    initialCurrentPage: String,
    initialTotalPages: String,
    onReviewChange: (String) -> Unit,
    onRatingChange: (Float) -> Unit,
    onCurrentPageChange: (String) -> Unit,
    onTotalPagesChange: (String) -> Unit
) {
    val volumeInfo = volume.volumeInfo
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
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
            text = "Published Date: ${volumeInfo.publishedDate}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = volumeInfo.description.take(300) + if (volumeInfo.description.length > 300
            ) "..." else "",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 5
        )

        // Code for review and rating
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Rating
            OutlinedTextField(
                value = initialRating.toString(),
                onValueChange = { newValueString ->
                    newValueString.toFloatOrNull()?.let {
                        onRatingChange(it)
                    }
                },
                label = { Text("Rating (1-5)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                modifier = Modifier.weight(1f)
            )

            // Current page
            OutlinedTextField(
                value = initialCurrentPage,
                onValueChange = { newPage ->
                    if (newPage.all { it.isDigit() } && newPage.length <= 5) {
                        onCurrentPageChange(newPage)
                    }
                },
                label = { Text("Page") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                modifier = Modifier.weight(1f)
            )

            // Total pages
            OutlinedTextField(
                value = initialTotalPages,
                onValueChange = { newTotal ->
                    if (newTotal.all { it.isDigit() } && newTotal.length <= 5) {
                        onTotalPagesChange(newTotal)
                    }
                },
                label = { Text("Total") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                modifier = Modifier.weight(1f)
            )
        }
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
    passedVolume: Volume,
    onBackPressed: () -> Unit,
    viewModel: ReadStackViewModel = getViewModel()
) {
    LaunchedEffect(passedVolume.id) {
        viewModel.loadBookDetails(passedVolume.id)
    }


    val bookFromDb by viewModel.currentBookDetails.collectAsStateWithLifecycle()

    val displayVolume = remember(passedVolume, bookFromDb) {
        val baseVolume = bookFromDb ?: passedVolume
        passedVolume.copy(
            status = baseVolume.status,
            review = baseVolume.review,
            rating = baseVolume.rating,
            currentPageNumber = baseVolume.currentPageNumber,
            totalPageNumber = baseVolume.totalPageNumber
        )
    }

    var reviewTextState by remember(displayVolume.review) {
        mutableStateOf(
            displayVolume.review ?: ""
        )
    }
    var ratingState by remember(displayVolume.rating) { mutableStateOf(displayVolume.rating) }
    var currentPageNumberState by remember(displayVolume.currentPageNumber) {
        mutableStateOf(
            displayVolume.currentPageNumber
        )
    }
    var totalPageNumberState by remember(displayVolume.totalPageNumber) {
        mutableStateOf(
            displayVolume.totalPageNumber
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(displayVolume.volumeInfo.title.take(20) + if (displayVolume.volumeInfo.title.length > 20) "... " else "") },
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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    BookDetailsScreenContent(
                        volume = displayVolume,
                        initialReview = reviewTextState,
                        initialRating = ratingState ?: 0f,
                        initialCurrentPage = currentPageNumberState?.toString() ?: "",
                        initialTotalPages = totalPageNumberState?.toString() ?: "",
                        onReviewChange = { reviewTextState = it },
                        onRatingChange = { ratingState = it },
                        onCurrentPageChange = { currentPageNumberState = it.toIntOrNull() },
                        onTotalPagesChange = { totalPageNumberState = it.toIntOrNull() }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.saveBook(
                            displayVolume,
                            BookStatus.READING,
                            reviewTextState,
                            ratingState,
                            currentPageNumberState,
                            totalPageNumberState
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mark as Reading")
                }
                Button(
                    onClick = {
                        viewModel.saveBook(
                            displayVolume,
                            BookStatus.READ,
                            reviewTextState,
                            ratingState,
                            currentPageNumberState,
                            totalPageNumberState
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mark as Read")
                }
                Button(
                    onClick = {
                        viewModel.saveBook(
                            displayVolume,
                            BookStatus.WANT_TO_READ,
                            reviewTextState,
                            ratingState,
                            currentPageNumberState,
                            totalPageNumberState
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Want to Read")
                }
                Button(
                    onClick = {
                        viewModel.saveBook(
                            displayVolume,
                            BookStatus.READING,
                            reviewTextState,
                            ratingState,
                            currentPageNumberState,
                            totalPageNumberState
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = reviewTextState != (displayVolume.review
                        ?: "") || bookFromDb == null
                ) {
                    Text(if (bookFromDb != null && bookFromDb?.review == reviewTextState) "Review Saved" else "Save Review")
                }
            }
        }
    }
}
