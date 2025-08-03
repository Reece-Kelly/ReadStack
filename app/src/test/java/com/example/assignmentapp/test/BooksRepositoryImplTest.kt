package com.example.assignmentapp.test

import com.example.assignmentapp.data.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class BooksRepositoryImplTest {

    private val booksAPI: BooksAPI = mockk()
    private val bookDao: BookDao = mockk()
    private lateinit var repository: BooksRepositoryImpl

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setUp() {
        mockkStatic(android.util.Log::class)
        every { android.util.Log.e(any(), any()) } returns 0
        every { android.util.Log.d(any(), any()) } returns 0

        repository = BooksRepositoryImpl(booksAPI, testDispatcher, bookDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getBooks returns mapped volumes`() = runTest(testScheduler) {
        val entity = BookEntity(
            id = "1",
            title = "Test Book",
            authors = listOf("Author"),
            description = "Desc",
            publisher = "Pub",
            publishedDate = "2023",
            thumbnail = "thumb",
            smallThumbnail = "smallThumb",
            status = BookStatus.READ,
            review = "Nice",
            rating = 5.0f,
            currentPageNumber = 50,
            totalPageNumber = 100
        )

        every { bookDao.getBooks() } returns flowOf(listOf(entity))

        val result = repository.getBooks().first()

        assertEquals(1, result.size)
        assertEquals("Test Book", result[0].volumeInfo.title)
    }

    @Test
    fun `saveBook inserts new book if not existing`() = runTest(testScheduler) {
        val volume = Volume("1", VolumeInfo("New Book"))
        coEvery { bookDao.getBookById("1") } returns flowOf(null)
        coEvery { bookDao.insert(any()) } just Runs

        repository.saveBook(volume, BookStatus.READING, "Great book", 4.5f, 10, 200)
        advanceUntilIdle()

        coVerify {
            bookDao.insert(
                withArg {
                    assertEquals("1", it.id)
                    assertEquals(BookStatus.READING, it.status)
                    assertEquals(4.5f, it.rating)
                }
            )
        }
    }

    @Test
    fun `searchBooks returns list on success`() = runTest(testScheduler) {
        val apiVolume = Volume("1", VolumeInfo("Search Book"))
        val responseBody = GoogleBooksApiResponse(listOf(apiVolume))

        coEvery { booksAPI.fetchBooks("search") } returns Response.success(responseBody)

        val result = repository.searchBooks("search")

        assertEquals(1, result.size)
        assertEquals("Search Book", result[0].volumeInfo.title)
    }

    @Test(expected = Exception::class)
    fun `searchBooks throws exception on failure`() = runTest(testScheduler) {
        coEvery { booksAPI.fetchBooks("search") } returns Response.error(400, mockk(relaxed = true))

        repository.searchBooks("search")
    }

    @Test
    fun `getRandomBookFromDb returns entity`() = runTest(testScheduler) {
        val entity = BookEntity(
            "1",
            "Book",
            listOf(),
            "",
            "",
            "",
            "",
            "",
            BookStatus.READ,
            100,
            200,
            5.0f,
            ""
        )
        coEvery { bookDao.getRandomBook() } returns entity

        val result = repository.getRandomBookFromDb()

        assertNotNull(result)
        assertEquals("1", result.id)
    }

    @Test
    fun `clearAllBooks calls dao`() = runTest(testScheduler) {
        coEvery { bookDao.clearAll() } just Runs

        repository.clearAllBooks()

        coVerify(exactly = 1) { bookDao.clearAll() }
    }
}
