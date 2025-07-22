package com.example.assignmentapp.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.assignmentapp.Title
import com.example.assignmentapp.data.Book
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.viewmodel.ReadStackViewModel


@Composable
fun BookList(
    viewModel: ReadStackViewModel,
    modifier: Modifier = Modifier,
    onBookClicked: (Volume) -> Unit,
) {
    val readStackUIState by viewModel.readStackUIState.collectAsStateWithLifecycle()
    val volumeItems = readStackUIState.volumes.items.orEmpty()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        item {
//            Title(title = "ReadStack")
//        }

        if (readStackUIState.isLoading) {
            item {
                CircularProgressIndicator()
            }
        }

        if (volumeItems.isNotEmpty()) {
            items(volumeItems) { volume ->
                VolumeListItem(
                    volume = volume,
                    onBookClicked = onBookClicked
                )
            }
        }

        readStackUIState.error?.let { errorMsg ->
            item {
                Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VolumeListItem(
    volume: Volume,
    onBookClicked: (Volume) -> Unit
) {
    val volumeInfo = volume.volumeInfo

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
                    onBookClicked(volume)
                }
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
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = volumeInfo.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            val authorsText = volumeInfo.authors?.joinToString(", ") ?: "Unknown Author"
            if (authorsText.isNotEmpty() && authorsText != "Unknown Author") {
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
