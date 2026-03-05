package com.example.app.data.dto

data class BookDto(
    val authorId: String,
    val name: String,
    val genres: List<String>,
    val publicationDate: String?,
    val rating: Int
)
