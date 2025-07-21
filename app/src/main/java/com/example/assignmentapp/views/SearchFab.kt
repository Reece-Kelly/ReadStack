package com.example.assignmentapp.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.assignmentapp.navigation.Screens

@Composable
fun SearchFab(navController: NavController) {
    FloatingActionButton(onClick = {
        navController.navigate(Screens.SearchScreen.route) {
            launchSingleTop = true
        }
    }) {
        Icon(Icons.Filled.Search, contentDescription = "Search")
    }
}
