package com.example.assignmentapp.data

class BooksRepositoryImpl : BooksRepository {
    override fun getBooks(): List<Book> {
        return listOf(
            Book("1984", "George Orwell", 1949, 250, 356, 4.5, "Great book!"),
            Book("Dune", "Frank Herbert", 1964, 200, 512, 4.0, "Sandy book! I like the worms!"),
            Book("Animal Farm", "George Orwell", 1945, 0, 192, 4.5, "Absolute power corrupts absolutely!"),
        )
    }
}