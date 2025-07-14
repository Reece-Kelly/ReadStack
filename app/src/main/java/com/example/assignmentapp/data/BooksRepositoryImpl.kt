package com.example.assignmentapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BooksRepositoryImpl(
    private  val booksAPI: BooksAPI,
    private val dispatcher: CoroutineDispatcher
): BooksRepository {

    override suspend fun getBooks(): NetworkResult<List<Book>> {
        return withContext(dispatcher) {
            try {
                val response = booksAPI.fetchBooks("harry potter")
                if (response.isSuccessful) {
                    NetworkResult.Success(response.body()!!)
                } else {
                    NetworkResult.Error(response.errorBody().toString())
                }
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}