package com.example.assignmentapp.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BooksRepositoryImpl(
    private val booksAPI: BooksAPI,
    private val dispatcher: CoroutineDispatcher
) : BooksRepository {

    override suspend fun getBooks(): NetworkResult<GoogleBooksApiResponse> {
        return withContext(dispatcher) {
            try {
                val response = booksAPI.fetchBooks("harry potter") // This is a placeholder string, will have to replace with a variable that the user controls (search bar)
                if (response.isSuccessful) {
                    Log.d("Network Response", response.body().toString())
                    print(response.body())
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