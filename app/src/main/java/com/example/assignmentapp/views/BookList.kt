package com.example.assignmentapp.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.preference.forEach
//import androidx.preference.isNotEmpty
import coil.compose.AsyncImage
import com.example.assignmentapp.data.Book
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.data.VolumeInfo
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import com.example.assignmentapp.views.ReadStackUIState
import org.koin.androidx.compose.koinViewModel



@Composable
fun BookList(modifier: Modifier) {
    val readStackViewModel: ReadStackViewModel = koinViewModel()
    val readStackUIState by readStackViewModel.readStackUIState.collectAsStateWithLifecycle()
    val volumeItems = readStackUIState.volumes.items.orEmpty()


    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = readStackUIState.isLoading
        ) {
            CircularProgressIndicator()
        }

        AnimatedVisibility(visible = volumeItems.isNotEmpty()) {
            LazyColumn {
                items(volumeItems) { volume ->
                    VolumeListItem(volume = volume)
                }
            }
        }

        AnimatedVisibility(
            visible = readStackUIState.error != null
        ) {
            Text(text = readStackUIState.error ?: "")
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VolumeListItem(volume: Volume) { // Renamed and changed parameter to Volume
    val volumeInfo = volume.volumeInfo // Convenience variable for easier access

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp) // Added horizontal padding for better spacing
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Book thumbnail (from VolumeInfo's imageLinks)
            val thumbnailUrl = volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
            if (thumbnailUrl != null) {
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = "${volumeInfo?.title ?: "Book"} cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), // Adjust height as needed
                    contentScale = ContentScale.Crop // Or ContentScale.Fit if you prefer
                )
                Spacer(modifier = Modifier.height(8.dp))
            }


            // Book title (from VolumeInfo)
            Text(
                text = volumeInfo?.title ?: "No Title Available",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Book authors (from VolumeInfo)
            // Join authors list into a string, handle null or empty list
            val authorsText = volumeInfo?.authors?.joinToString(", ") ?: "Unknown Author"
            if (authorsText.isNotEmpty() && authorsText != "Unknown Author") { // Only show if authors exist
                Text(
                    text = "by $authorsText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
