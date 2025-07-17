package com.example.assignmentapp

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.assignmentapp.views.BookList
import com.example.assignmentapp.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.getViewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainActivity.applicationContext)
            modules(appModules)
        }
            Log.d("MainActivity", "onCreate Called")
            setContent {
                AssignmentAppTheme {
                    ReadStackApp()
                }
            }
        }
    }


@Composable
private fun ReadStackApp() {
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
        // Use BookList directly without wrapping it in a Column or Box
        BookList(
            viewModel = readStackViewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 72.dp) // Account for FAB space
        )
    }
}


    @Preview
    @Composable
    fun ReadStackAppPreview() {
        ReadStackApp()
    }

    @Composable
    fun HomeScreen() {
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

                    Text(
                        text = "Currently Reading:",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                    )

                    BookButton(
                        title = "1984",
                        author = "George Orwell",
                        currentPageNumber = 250,
                        totalPageNumber = 356,
                        onClick = {}
                    )

                    BookButton(
                        title = "Dune",
                        author = "Frank Herbert",
                        currentPageNumber = 300,
                        totalPageNumber = 412,
                        onClick = {}
                    )

                    RecommendationButton(
                        title = "Get a book recommendation!",
                        onClick = {}
                    )
                }
            }
        }
    }

    @Composable
    fun BookScreen() {
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

                    Text(
                        text = "Currently Reading:",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                    )

                    BookButton(
                        title = "1984",
                        author = "George Orwell",
                        currentPageNumber = 250,
                        totalPageNumber = 356,
                        onClick = {}
                    )

                    BookButton(
                        title = "Dune",
                        author = "Frank Herbert",
                        currentPageNumber = 300,
                        totalPageNumber = 412,
                        onClick = {}
                    )

                    Text(
                        text = "Want To Read:",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                    )

                    BookButton(
                        title = "Animal Farm",
                        author = "George Orwell",
                        currentPageNumber = 0,
                        totalPageNumber = 192,
                        onClick = {}
                    )

                    BookButton(
                        title = "Grit",
                        author = "Angela Duckworth",
                        currentPageNumber = 0,
                        totalPageNumber = 412,
                        onClick = {}
                    )

                    Text(
                        text = "Read:",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                    )

                    BookButton(
                        title = "The 7 Habits of Highly Effective People",
                        author = "Stephen R. Covey",
                        currentPageNumber = 412,
                        totalPageNumber = 412,
                        onClick = {}
                    )


                }
            }
        }
    }

    @Composable
    fun BookInformationScreen() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = { }) {
                    Icon(Icons.Filled.Search, contentDescription = "Add")
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            bottomBar = {
                BottomNavBar()
            }
        ) { innerPadding ->
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

                    BookInfo(
                        title = "1984",
                        author = "George Orwell",
                        yearPublished = 1949,
                        rating = 4.5,

                        review = "1984 by George Orwell is a haunting and prophetic novel that explores the terrifying consequences of totalitarianism and surveillance. Its bleak portrayal of a dystopian future where truth is manipulated and individuality is crushed remains chillingly relevant. Orwell's masterful storytelling and incisive political commentary make it an essential and unsettling read.",
                        currentPage = 250,
                        totalPages = 356,
                        onPageUpdate = {}
                    )

                    BookButton(
                        title = "The 7 Habits of Highly Effective People",
                        author = "Stephen R. Covey",
                        currentPageNumber = 412,
                        totalPageNumber = 412,
                        onClick = {}
                    )

                }
            }
        }
    }

    @Composable
    fun RecommendationScreen() {
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

                    Text(
                        text = "Book recommendations based on what you have read:",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                    )

                    BookButton(
                        title = "Dune Messiah",
                        author = "Frank Herbert",
                        currentPageNumber = 0,
                        totalPageNumber = 412,
                        onClick = {}
                    )

                    BookButton(
                        title = "Homage to Catalonia",
                        author = "George Orwell",
                        currentPageNumber = 0,
                        totalPageNumber = 275,
                        onClick = {}
                    )

                    BookButton(
                        title = "First Things First",
                        author = "Stephen R. Covey",
                        currentPageNumber = 0,
                        totalPageNumber = 275,
                        onClick = {}
                    )

                }
            }
        }
    }

    @Composable
    fun SearchScreen() {
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

                    Text(
                        text = "Search - Stephen King|",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                    )

                    BookButton(
                        title = "The Stand",
                        author = "Stephen King",
                        currentPageNumber = 0,
                        totalPageNumber = 1275,
                        onClick = {}
                    )

                    BookButton(
                        title = "It",
                        author = "Stephen King",
                        currentPageNumber = 0,
                        totalPageNumber = 1168,
                        onClick = {}
                    )

                    BookButton(
                        title = "The Shining",
                        author = "Stephen King",
                        currentPageNumber = 0,
                        totalPageNumber = 447,
                        onClick = {}
                    )
                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun HomeScreenPreview() {
        AssignmentAppTheme {
            HomeScreen()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun BookScreenPreview() {
        AssignmentAppTheme {
            BookScreen()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun BookInformationPreview() {
        AssignmentAppTheme {
            BookInformationScreen()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun RecommendationPreview() {
        AssignmentAppTheme {
            RecommendationScreen()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SearchPreview() {
        AssignmentAppTheme {
            SearchScreen()
        }
    }


    @Composable
    fun Title(title: String, modifier: Modifier = Modifier) {
        Text(
            text = title,
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(16.dp)
        )
    }

    @Composable
    fun BookButton(
        title: String,
        currentPageNumber: Int,
        totalPageNumber: Int,
        author: String,
        onClick: () -> Unit
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                "$title | $author\nUp to page $currentPageNumber of $totalPageNumber",
                style = TextStyle(fontSize = 23.sp)
            )
        }
    }


    @Composable
    fun RecommendationButton(title: String, onClick: () -> Unit) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                title,
                style = TextStyle(fontSize = 20.sp)
            )
        }
    }

    @Composable
    fun BookInfo(
        title: String,
        author: String,
        yearPublished: Int,
        rating: Double,
        review: String,
        currentPage: Int,
        totalPages: Int,
        onPageUpdate: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Book Information",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Title: $title", fontSize = 18.sp)
                Text("Author: $author", fontSize = 18.sp)
                Text("Rating: $rating/5 Stars", fontSize = 18.sp)

                TextButton(
                    onClick = {},
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Add another rating",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }

                Text("Year Published: $yearPublished", fontSize = 18.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Review:", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Text(review, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onPageUpdate,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Page $currentPage / $totalPages")
                }
            }
        }
    }


    @Composable
    fun BottomNavBar(modifier: Modifier = Modifier) {
        val items = listOf("Home", "Books", "Suggest")
        var selectedItem by remember { mutableIntStateOf(0) }

        NavigationBar(modifier = modifier) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        when (item) {
                            "Home" -> Icon(Icons.Default.Home, contentDescription = item)
                            "Books" -> Icon(
                                Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = item
                            )

                            "Suggest" -> Icon(Icons.Default.Lightbulb, contentDescription = item)
                        }
                    },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    }