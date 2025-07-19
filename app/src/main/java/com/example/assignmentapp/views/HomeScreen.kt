package com.example.assignmentapp.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.assignmentapp.BottomNavBar
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(onBookClicked: (Volume) -> Unit) {
    val readStackViewModel: ReadStackViewModel = getViewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavBar()
        }
    ) { innerPadding ->

        BookList(
            viewModel = readStackViewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 72.dp),
            onBookClicked = onBookClicked // Not too sure about this line
        )
    }
}