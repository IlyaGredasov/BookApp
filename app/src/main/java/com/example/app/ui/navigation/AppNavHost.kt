package com.example.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app.data.BooksApi
import com.example.app.data.BooksPrefs
import com.example.app.domain.Book
import com.example.app.ui.screens.books.BookList
import com.example.app.ui.screens.books.BookScreen
import com.example.app.ui.screens.books.BooksViewModel

@Composable
fun AppNavHost(
    api: BooksApi,
    prefs: BooksPrefs,
    initialBook: Book? = null
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            val booksViewModel: BooksViewModel = viewModel(
                factory = BooksViewModel.factory(context, prefs, api)
            )
            BookList(
                booksViewModel = booksViewModel,
                onBookClick = { book ->
                    navController.navigate(
                        "book/${book.id}/${book.title}/${book.author}/${book.genre}/${book.publicationDate}/${book.rating}"
                    )
                },
            )
        }

        composable(
            route = "book/{id}/{title}/{author}/{genre}/{year}/{rating}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
                navArgument("title") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType },
                navArgument("genre") { type = NavType.StringType },
                navArgument("year") { type = NavType.StringType },
                navArgument("rating") { type = NavType.StringType })
        ) { backStack ->
            val args = backStack.arguments!!
            val book = Book(
                id = args.getLong("id"),
                title = args.getString("title").orEmpty(),
                author = args.getString("author").orEmpty(),
                genre = args.getString("genre").orEmpty(),
                publicationDate = args.getString("year").orEmpty(),
                rating = args.getString("rating").orEmpty()
            )
            BookScreen(book)
        }
    }

    if (initialBook != null) {
        androidx.compose.runtime.LaunchedEffect(initialBook) {
            navController.navigate(
                "book/${initialBook.id}/${initialBook.title}/${initialBook.author}/${initialBook.genre}/${initialBook.publicationDate}/${initialBook.rating}"
            )
        }
    }
}
