package com.example.app.data.db

import androidx.room.Entity

@Entity(
    tableName = "custom_books",
    primaryKeys = ["baseBookId", "userId"]
)
data class CustomBookEntity(
    val baseBookId: Long, // ID базовой книги из таблицы books
    val userId: Long,
    val authorId: String,
    val name: String,
    val genres: String,
    val publicationDate: String?,
    val rating: Int
)
