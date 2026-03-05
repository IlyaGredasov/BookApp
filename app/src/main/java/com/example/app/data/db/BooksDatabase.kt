package com.example.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        BookEntity::class,
        AuthorEntity::class,
        UserEntity::class,
        CustomBookEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun booksDao(): BooksDao

    companion object {
        @Volatile
        private var INSTANCE: BooksDatabase? = null

        fun getInstance(context: Context): BooksDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BooksDatabase::class.java,
                    "books.db"
                ).build().also { INSTANCE = it }
            }
    }
}
