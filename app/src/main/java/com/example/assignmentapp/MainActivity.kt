package com.example.assignmentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.assignmentapp.ui.theme.AssignmentAppTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssignmentAppTheme {
                    MainScreen()
                }
            }
        }
    }


@Composable
fun MainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 72.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(title = "ReadStack")

                BookButton(
                    title = "Example Book",
                    currentPageNumber = 300,
                    totalPageNumber = 560,
                    onClick = {}
                )
            }

            BottomNavBar(
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun Title(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(16.dp)
    )
}

@Composable
fun BookButton(title: String, currentPageNumber: Int, totalPageNumber: Int, onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text("$title page number $currentPageNumber/$totalPageNumber")
    }
}


@Composable
fun BottomNavBar(modifier: Modifier = Modifier) {
    val items = listOf("Home", "Search", "Profile")
    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(modifier = modifier) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        "Home" -> Icon(Icons.Default.Home, contentDescription = item)
                        "Search" -> Icon(Icons.Default.Search, contentDescription = item)
                        "Profile" -> Icon(Icons.Default.Person, contentDescription = item)
                    }
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    AssignmentAppTheme {
//        Greeting("Hello")
//    }
//}