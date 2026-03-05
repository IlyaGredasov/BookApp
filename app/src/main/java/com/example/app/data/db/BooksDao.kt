package com.example.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BooksDao {
    @Query("SELECT * FROM books")
    suspend fun getBooks(): List<BookEntity>

    @Query("SELECT COUNT(*) FROM books WHERE LOWER(name) = LOWER(:title)")
    suspend fun countBooksByTitle(title: String): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBook(entity: BookEntity)

    @Upsert
    suspend fun upsertBook(entity: BookEntity)

    @Query("SELECT * FROM authors")
    suspend fun getAuthors(): List<AuthorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthor(author: AuthorEntity)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUser(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM custom_books WHERE baseBookId = :baseBookId AND userId = :userId")
    suspend fun getCustomBook(baseBookId: Long, userId: Long): CustomBookEntity?

    @Query("SELECT * FROM custom_books WHERE userId = :userId")
    suspend fun getCustomBooksByUser(userId: Long): List<CustomBookEntity>

    @Upsert
    suspend fun upsertCustomBook(entity: CustomBookEntity)

    @Query("DELETE FROM custom_books WHERE baseBookId = :baseBookId AND userId = :userId")
    suspend fun deleteCustomBook(baseBookId: Long, userId: Long)
}
