package com.example.assignmentapp.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assignmentapp.data.Volume
import com.example.assignmentapp.views.BookDetailsScreen
import com.example.assignmentapp.views.HomeScreen
import com.example.assignmentapp.views.SearchScreen
import com.example.assignmentapp.views.SuggestScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(Screens.HomeScreen.route) {
            HomeScreen(
                onBookClicked = { volume ->
                    try {
                        val json = Json.encodeToString(volume)
                        val encoded = Uri.encode(json)
                        navController.navigate(
                            "${Screens.BookDetailsScreen.route}/$encoded"
                        )
                    } catch (e: Exception) {
                        Log.e("Navigation", "Failed to encode volume", e)
                    }
                },
                navController = navController
            )
        }

        composable(Screens.SearchScreen.route) {
            SearchScreen(
                onBookClicked = { volume ->
                    val json = Json.encodeToString(volume)
                    val encoded = Uri.encode(json)
                    navController.navigate("${Screens.BookDetailsScreen.route}/$encoded")
                },
                navController = navController
            )
        }

        composable(Screens.SuggestScreen.route) {
            SuggestScreen(
                onBookClicked = { volume ->
                    val json = Json.encodeToString(volume)
                    val encoded = Uri.encode(json)
                    navController.navigate("${Screens.BookDetailsScreen.route}/$encoded")
                },
                navController = navController
            )
        }

        composable(
            route = "${Screens.BookDetailsScreen.route}/{volumeJson}",
            arguments = listOf(
                navArgument("volumeJson") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val volumeJsonString = backStackEntry.arguments?.getString("volumeJson")
            var volume: Volume? = null
            var errorOccurred = false

            if (volumeJsonString != null) {
                try {
                    val decodedJson = Uri.decode(volumeJsonString)
                    volume = Json.decodeFromString<Volume>(decodedJson)
                } catch (e: Exception) {
                    Log.e("Navigation", "BookDetails: Failed to decode volumeJson", e)
                    errorOccurred = true
                }
            } else {
                Log.e("Navigation", "BookDetails: volumeJsonString is null")
                errorOccurred = true
            }

            if (errorOccurred || volume == null) {
                Text("Error loading book details.")
            } else {
                BookDetailsScreen(
                    passedVolume = volume,
                    onBackPressed = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
