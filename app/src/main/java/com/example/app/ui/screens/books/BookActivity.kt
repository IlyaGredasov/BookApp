package com.example.app.ui.screens.books

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.app.data.BooksPrefs
import com.example.app.data.Net
import com.example.app.ui.navigation.AppNavHost
import com.example.app.ui.theme.BookAppTheme
import com.example.app.domain.Book

class BookActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = BooksPrefs(this)
        val api = Net.api

        val initialBook: Book? = if (intent?.getBooleanExtra("fromNotification", false) == true) {
            Book(
                id = intent.getLongExtra("id", 0L),
                title = intent.getStringExtra("title").orEmpty(),
                author = intent.getStringExtra("author").orEmpty(),
                genre = intent.getStringExtra("genre").orEmpty(),
                publicationDate = intent.getStringExtra("year").orEmpty(),
                rating = intent.getStringExtra("rating").orEmpty()
            )
        } else {
            null
        }

        setContent {
            BookAppTheme(dynamicColor = false) {
                AppNavHost(api = api, prefs = prefs, initialBook = initialBook)
            }
        }
    }
}
