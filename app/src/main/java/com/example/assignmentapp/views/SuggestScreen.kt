package com.example.assignmentapp.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.assignmentapp.Title
import com.example.assignmentapp.views.BottomNavBar
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.navigation.Screens
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestScreen(
    onBookClicked: (Volume) -> Unit,
    navController: NavController
) {
    val readStackViewModel: ReadStackViewModel = getViewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
//        floatingActionButton = { SearchFab(navController) },
//        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavBar(
                modifier = Modifier,
                navController = navController
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Title(title = "ReadStack")

            Text(
                text = "The Suggest Screen! (This is a placeholder)",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(innerPadding)
            )
        }
    }
}