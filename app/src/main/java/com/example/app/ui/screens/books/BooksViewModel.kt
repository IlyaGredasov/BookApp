package com.example.app.ui.screens.books

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app.data.BooksApi
import com.example.app.data.BooksPrefs
import com.example.app.data.db.AuthorEntity
import com.example.app.data.db.BookEntity
import com.example.app.data.db.BooksDao
import com.example.app.data.db.BooksDatabase
import com.example.app.data.db.CustomBookEntity
import com.example.app.data.parseRoot
import com.example.app.domain.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksViewModel(
    private val appContext: Context,
    private val dao: BooksDao,
    private val prefs: BooksPrefs,
    private val api: BooksApi
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books = _books.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (!prefs.initialized) {
                val json = api.getRoot()
                val root = parseRoot(json)

                root.authors.forEach { a ->
                    dao.insertAuthor(
                        AuthorEntity(
                            id = a.id,
                            author = a.author
                        )
                    )
                }

                root.books.forEach { b ->
                    dao.insertBook(
                        BookEntity(
                            id = 0,
                            authorId = b.authorId,
                            name = b.name,
                            genres = b.genres.joinToString(", "),
                            publicationDate = b.publicationDate,
                            rating = b.rating
                        )
                    )
                }

                prefs.initialized = true
            }

            _books.value = loadBooksFromDb()
        }
    }

    private suspend fun loadBooksFromDb(): List<Book> {
        val authors = dao.getAuthors()
        val authorMap = authors.associate { it.id to it.author }
        val currentUserId = prefs.currentUserId

        val baseBooks = dao.getBooks()
        
        val customBooksMap = if (currentUserId != null) {
            dao.getCustomBooksByUser(currentUserId).associateBy { it.baseBookId }
        } else {
            emptyMap()
        }

        return baseBooks.map { baseBook ->
            val customBook = customBooksMap[baseBook.id]
            
            val name: String
            val authorId: String
            val genres: String
            val publicationDate: String?
            val rating: Int
            
            if (customBook != null) {
                name = customBook.name
                authorId = customBook.authorId
                genres = customBook.genres
                publicationDate = customBook.publicationDate
                rating = customBook.rating
            } else {
                name = baseBook.name
                authorId = baseBook.authorId
                genres = baseBook.genres
                publicationDate = baseBook.publicationDate
                rating = baseBook.rating
            }
            
            Book(
                id = baseBook.id,
                title = name,
                author = authorMap[authorId].orEmpty(),
                genre = genres,
                publicationDate = publicationDate.orEmpty(),
                rating = rating.toString()
            )
        }.sortedBy { it.author }
    }

    fun updateBook(
        id: Long,
        newTitle: String,
        newAuthor: String,
        newGenre: String,
        newYear: String,
        newRating: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (newTitle.isBlank() || newAuthor.isBlank())
                return@launch

            val currentUserId = prefs.currentUserId ?: return@launch

            val authorId = newAuthor.lowercase()
            dao.insertAuthor(
                AuthorEntity(
                    id = authorId,
                    author = newAuthor
                )
            )
            
            dao.upsertCustomBook(
                CustomBookEntity(
                    baseBookId = id,
                    userId = currentUserId,
                    authorId = authorId,
                    name = newTitle,
                    genres = newGenre,
                    publicationDate = newYear.ifBlank { null },
                    rating = newRating.toIntOrNull() ?: 0
                )
            )

            _books.value = loadBooksFromDb()

            val updatedBook = Book(
                id = id,
                title = newTitle,
                author = newAuthor,
                genre = newGenre,
                publicationDate = newYear,
                rating = newRating
            )

            BookNotificationHelper.showBookUpdatedNotification(appContext, updatedBook)
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun factory(context: Context, prefs: BooksPrefs, api: BooksApi) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return BooksViewModel(
                        appContext = context.applicationContext,
                        dao = BooksDatabase.getInstance(context).booksDao(),
                        prefs = prefs,
                        api = api
                    ) as T
                }
            }
    }
}
