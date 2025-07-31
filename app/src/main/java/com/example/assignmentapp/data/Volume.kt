package com.example.assignmentapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GoogleBooksApiResponse(
    @SerialName("items")
    val items: List<Volume>?
)

@Serializable
data class Volume(
    @SerialName("id")
    val id: String = "",
    @SerialName("volumeInfo")
    val volumeInfo: VolumeInfo = VolumeInfo(),
    @SerialName("bookStatus")
    val status: BookStatus? = null,
    @SerialName("bookReview")
    var review: String? = null,
    @SerialName("bookRating")
    var rating: Float? = null,
    @SerialName("bookCurrentPageNumber")
    var currentPageNumber: Int? = 0,
    @SerialName("bookTotalPageNumber")
    var totalPageNumber: Int? = 0,
)

@Serializable
data class VolumeInfo(
    @SerialName("title")
    val title: String = "",
    @SerialName("authors")
    val authors: List<String>? = emptyList(),
    @SerialName("description")
    val description: String = "",
    @SerialName("publisher")
    val publisher: String = "",
    @SerialName("publishedDate")
    val publishedDate: String = "",
    @SerialName("imageLinks")
    val imageLinks: ImageLinks? = ImageLinks()
)

@Serializable
data class ImageLinks(
    @SerialName("thumbnail")
    val thumbnail: String = "",
    @SerialName("smallThumbnail")
    val smallThumbnail: String = ""
)
