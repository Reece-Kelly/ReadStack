package com.example.assignmentapp.test

import android.util.Log
import com.example.assignmentapp.data.*
import com.example.assignmentapp.viewmodel.ReadStackViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ReadStackViewModelTest {

    private val booksRepository: BooksRepository = mockk()
    private lateinit var viewModel: ReadStackViewModel

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { booksRepository.getBooks() } returns flowOf(emptyList())
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        viewModel = ReadStackViewModel(booksRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `observeBooksInDb updates uiState with volumes`() = runTest {
        val volumes = listOf(
            Volume("1", VolumeInfo("Book One")),
            Volume("2", VolumeInfo("Book Two"))
        )
        every { booksRepository.getBooks() } returns flowOf(volumes)

        viewModel = ReadStackViewModel(booksRepository)

        advanceUntilIdle()

        val state = viewModel.readStackUIState.value
        assertEquals(false, state.isLoading)
        assertEquals(volumes, state.volumes)
        assertNull(state.error)
    }

    @Test
    fun `searchBooks sets loading and updates uiState on success`() = runTest {
        val query = "kotlin"
        val searchResults = listOf(
            Volume("3", VolumeInfo("Kotlin Book"))
        )
        coEvery { booksRepository.searchBooks(query) } returns searchResults

        viewModel.searchBooks(query)

        assertEquals(true, viewModel.readStackUIState.value.isLoading)

        advanceUntilIdle()

        val state = viewModel.readStackUIState.value
        assertEquals(false, state.isLoading)
        assertEquals(searchResults, state.searchResults)
        assertEquals(query, state.currentSearchWord)
        assertNull(state.error)
    }

    @Test
    fun `searchBooks sets error on failure`() = runTest {
        val query = "fail"
        val exception = Exception("Network error")
        coEvery { booksRepository.searchBooks(query) } throws exception

        viewModel.searchBooks(query)

        advanceUntilIdle()

        val state = viewModel.readStackUIState.value
        assertEquals(false, state.isLoading)
        assertEquals("Network error", state.error)
    }

    @Test
    fun `loadBookDetails updates currentBookDetails when book found`() = runTest {
        val bookId = "1"
        val volumes = listOf(
            Volume("1", VolumeInfo("Found Book")),
            Volume("2", VolumeInfo("Other Book"))
        )
        every { booksRepository.getBooks() } returns flowOf(volumes)

        viewModel.loadBookDetails(bookId)

        advanceUntilIdle()

        val currentDetails = viewModel.currentBookDetails.value
        assertEquals(bookId, currentDetails?.id)
        assertEquals("Found Book", currentDetails?.volumeInfo?.title)
    }

    @Test
    fun `loadBookDetails sets currentBookDetails to null when book not found`() = runTest {
        val bookId = "missing"
        every { booksRepository.getBooks() } returns flowOf(emptyList())

        viewModel.loadBookDetails(bookId)

        advanceUntilIdle()

        val currentDetails = viewModel.currentBookDetails.value
        assertNull(currentDetails)
    }

    @Test
    fun `saveBook calls repository saveBook and loads book details`() = runTest {
        val volume = Volume("1", VolumeInfo("To Save"))
        coEvery { booksRepository.saveBook(any(), any(), any(), any(), any(), any()) } just Runs
        val volumes = listOf(volume)
        every { booksRepository.getBooks() } returns flowOf(volumes)

        viewModel.saveBook(volume, BookStatus.READING, "Review", 4.5f, 10, 100)

        advanceUntilIdle()

        coVerify {
            booksRepository.saveBook(
                match { it.id == "1" && it.review == "Review" && it.rating == 4.5f },
                BookStatus.READING,
                "Review",
                4.5f,
                10,
                100
            )
        }
        assertEquals(volume, viewModel.currentBookDetails.value)
    }

    @Test
    fun `saveBook updates uiState error on exception`() = runTest {
        val volume = Volume("1", VolumeInfo("Fail Save"))
        val exception = Exception("Save failed")
        coEvery { booksRepository.saveBook(any(), any(), any(), any(), any(), any()) } throws exception

        viewModel.saveBook(volume, BookStatus.READING, null, null, null, null)

        advanceUntilIdle()

        val error = viewModel.readStackUIState.value.error
        assertTrue(error?.contains("Save failed") == true)
    }

    @Test
    fun `clearDatabase calls repository clearAllBooks`() = runTest {
        coEvery { booksRepository.clearAllBooks() } just Runs

        viewModel.clearDatabase()

        advanceUntilIdle()

        coVerify(exactly = 1) { booksRepository.clearAllBooks() }
    }

    @Test
    fun `getRandomBookFromDb returns book entity`() = runTest {
        val bookEntity = BookEntity("1", "Title", emptyList(), "", "", "", "", "", BookStatus.READ, 0, 0, 0f, "")
        coEvery { booksRepository.getRandomBookFromDb() } returns bookEntity

        val result = viewModel.getRandomBookFromDb()

        assertEquals(bookEntity, result)
    }

    @Test
    fun `getRandomHighlyRatedBook returns book entity`() = runTest {
        val bookEntity = BookEntity("2", "Title2", emptyList(), "", "", "", "", "", BookStatus.READ, 0, 0, 0f, "")
        coEvery { booksRepository.getRandomHighlyRatedBook(4.0f) } returns bookEntity

        val result = viewModel.getRandomHighlyRatedBook(4.0f)

        assertEquals(bookEntity, result)
    }

    @Test
    fun `getRandomHighlyRatedBook sets error and returns null on exception`() = runTest {
        val exception = Exception("fail")
        coEvery { booksRepository.getRandomHighlyRatedBook(any()) } throws exception

        val result = viewModel.getRandomHighlyRatedBook(4.0f)

        assertNull(result)
        assertTrue(viewModel.readStackUIState.value.error?.contains("fail") == true)
    }
}
