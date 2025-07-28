package com.example.assignmentapp.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BooksRepositoryImpl(
    private val booksAPI: BooksAPI,
    private val dispatcher: CoroutineDispatcher,
    private val bookDao: BookDao
) : BooksRepository {

    override fun getBooks(): Flow<List<Volume>> {
        return bookDao.getBooks().map { bookEntities ->
            bookEntities.map { entity ->
                Volume(
                    id = entity.id,
                    volumeInfo = VolumeInfo(
                        title = entity.title,
                        authors = entity.authors,
                        description = entity.description,
                        publisher = entity.publisher,
                        publishedDate = entity.publishedDate,
                        imageLinks = ImageLinks(
                            thumbnail = entity.thumbnail,
                            smallThumbnail = entity.smallThumbnail
                        )
                    ),
                    status = entity.status
                )
            }
        }
    }

    override suspend fun fetchRemoteBooks(query: String) {
        withContext(dispatcher) {
            try {
                val response = booksAPI.fetchBooks(query)
                if (response.isSuccessful) {
                    response.body()?.items?.forEach { volume ->
                        bookDao.insert(
                            BookEntity(
                                id = volume.id,
                                title = volume.volumeInfo.title,
                                authors = volume.volumeInfo.authors ?: emptyList(),
                                description = volume.volumeInfo.description,
                                publisher = volume.volumeInfo.publisher,
                                publishedDate = volume.volumeInfo.publishedDate,
                                thumbnail = volume.volumeInfo.imageLinks?.thumbnail ?: "",
                                smallThumbnail = volume.volumeInfo.imageLinks?.smallThumbnail ?: "",
                                status = null,
                                currentPageNumber = 0,
                                totalPageNumber = 0,
                                rating = null,
                                review = null
                            )
                        )
                    }
                } else {
                    Log.e("BooksRepository", "API Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("BooksRepository", "Exception: ${e.message}")
            }
        }
    }

    override suspend fun saveBook(volume: Volume, status: BookStatus) {
        withContext(dispatcher) {
            bookDao.insert(
                BookEntity(
                    id = volume.id,
                    title = volume.volumeInfo.title,
                    authors = volume.volumeInfo.authors ?: emptyList(),
                    description = volume.volumeInfo.description,
                    publisher = volume.volumeInfo.publisher,
                    publishedDate = volume.volumeInfo.publishedDate,
                    thumbnail = volume.volumeInfo.imageLinks?.thumbnail ?: "",
                    smallThumbnail = volume.volumeInfo.imageLinks?.smallThumbnail ?: "",
                    status = status,
                    currentPageNumber = 0,
                    totalPageNumber = 0,
                    rating = null,
                    review = null
                )
            )
        }
    }


    override suspend fun searchBooks(query: String): List<Volume> {
        val response = booksAPI.fetchBooks(query)
        return if (response.isSuccessful) {
            response.body()?.items?.map { apiVolume ->
                Volume(
                    id = apiVolume.id,
                    volumeInfo = apiVolume.volumeInfo,
                    status = null
                )
            } ?: emptyList()
        } else {
            throw Exception("Search failed: ${response.errorBody()?.string()}")
        }
    }
}


