package com.example.assignmentapp.navigation

sealed class Screens (val route: String) {
    object HomeScreen: Screens("home")
    object BooksScreen: Screens("booksScreen")
}