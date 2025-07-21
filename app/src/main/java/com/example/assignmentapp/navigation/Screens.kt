package com.example.assignmentapp.navigation

sealed class Screens (val route: String) {
    object HomeScreen: Screens("home")
    object BookDetailsScreen: Screens("bookDetails")
    object SearchScreen: Screens("searchScreen")
    object SuggestScreen: Screens("suggestScreen")
}