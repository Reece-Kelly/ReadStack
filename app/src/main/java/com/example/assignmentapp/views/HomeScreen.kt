package com.example.assignmentapp.views

import androidx.compose.foundation.layout.Column
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
import androidx.navigation.NavController
import com.example.assignmentapp.Title
import com.example.assignmentapp.views.BottomNavBar
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import org.koin.androidx.compose.getViewModel
import com.example.assignmentapp.navigation.Screens
import com.example.assignmentapp.navigation.AppNavigation

@Composable
fun HomeScreen(
    onBookClicked: (Volume) -> Unit,
    navController: NavController
) {
    val readStackViewModel: ReadStackViewModel = getViewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screens.SearchScreen.route)
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Title(title = "ReadStack")

            BookList(
                viewModel = readStackViewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 72.dp),
                onBookClicked = onBookClicked
            )
        }
    }
}