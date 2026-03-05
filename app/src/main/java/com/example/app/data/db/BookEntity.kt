package com.example.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorId: String,
    val name: String,
    val genres: String,
    val publicationDate: String?,
    val rating: Int
)
