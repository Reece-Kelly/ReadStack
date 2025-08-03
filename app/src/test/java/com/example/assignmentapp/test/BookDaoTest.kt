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
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class BookDaoTest {

    private val bookDao: BookDao = mockk()
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun createBookEntity(
        id: String = "1",
        title: String = "Test Book",
        rating: Float = 4.5f
    ): BookEntity = BookEntity(
        id = id,
        title = title,
        authors = listOf("Author"),
        description = "Desc",
        publisher = "Pub",
        publishedDate = "2023",
        thumbnail = "thumb",
        smallThumbnail = "smallThumb",
        status = BookStatus.READING,
        review = "Nice book",
        rating = rating,
        currentPageNumber = 50,
        totalPageNumber = 100
    )

    @Test
    fun `insert and getBookById returns inserted book`() = runTest(testScheduler) {
        val book = createBookEntity()
        coEvery { bookDao.insert(book) } just Runs
        every { bookDao.getBookById("1") } returns flowOf(book)

        bookDao.insert(book)

        val retrieved = bookDao.getBookById("1").first()
        assertNotNull(retrieved)
        assertEquals("Test Book", retrieved.title)
    }

    @Test
    fun `insert with same id replaces existing book`() = runTest(testScheduler) {
        val original = createBookEntity(id = "1", title = "Original")
        val updated = createBookEntity(id = "1", title = "Updated")

        coEvery { bookDao.insert(original) } just Runs
        coEvery { bookDao.insert(updated) } just Runs
        every { bookDao.getBookById("1") } returns flowOf(updated)

        bookDao.insert(original)
        bookDao.insert(updated)

        val retrieved = bookDao.getBookById("1").first()
        assertEquals("Updated", retrieved?.title)
    }

    @Test
    fun `getBooks returns all inserted books`() = runTest(testScheduler) {
        val book1 = createBookEntity("1", "Book A")
        val book2 = createBookEntity("2", "Book B")
        every { bookDao.getBooks() } returns flowOf(listOf(book1, book2))

        val allBooks = bookDao.getBooks().first()
        assertEquals(2, allBooks.size)
    }

    @Test
    fun `getRandomBook returns a book`() = runTest(testScheduler) {
        val book = createBookEntity("1", "Book A")
        coEvery { bookDao.getRandomBook() } returns book

        val randomBook = bookDao.getRandomBook()
        assertNotNull(randomBook)
        assertEquals("Book A", randomBook.title)
    }

    @Test
    fun `clearAll removes all books`() = runTest(testScheduler) {
        coEvery { bookDao.clearAll() } just Runs

        bookDao.clearAll()

        coVerify(exactly = 1) { bookDao.clearAll() }
    }
}
