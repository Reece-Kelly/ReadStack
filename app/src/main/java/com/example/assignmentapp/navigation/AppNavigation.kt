package com.example.assignmentapp.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.assignmentapp.views.HomeScreen
import com.example.assignmentapp.views.BookDetailsScreen
import com.example.assignmentapp.data.Volume
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

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
                }
            )
        }

        composable(
            route = "${Screens.BookDetailsScreen.route}/{volume}",
            arguments = listOf(
                navArgument("volume") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val volumeJson = backStackEntry.arguments?.getString("volume") ?: ""
            val volume = try {
                val decoded = Uri.decode(volumeJson)
                Json.decodeFromString<Volume>(decoded)
            } catch (e: Exception) {
                Log.e("Navigation", "Failed to decode volume", e)
                Volume()
            }

            BookDetailsScreen(
                volume = volume,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}
