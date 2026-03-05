package com.example.app.ui.screens.books

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.app.domain.Book
import kotlinx.coroutines.launch

sealed class Row {
    data class Header(val letter: Char) : Row()
    data class BookRow(val book: Book) : Row()
}

@Composable
fun BookList(
    booksViewModel: BooksViewModel,
    onBookClick: (Book) -> Unit
) {
    var editingBook by remember { mutableStateOf<Book?>(null) }

    val books by booksViewModel.books.collectAsState()

    val rows = remember(books) {
        buildList {
            var last: Char? = null
            for (b in books.sortedBy { it.author }) {
                val letter = b.author.firstOrNull { it.isLetter() }?.uppercaseChar() ?: '#'
                if (last != letter) {
                    add(Row.Header(letter))
                    last = letter
                }
                add(Row.BookRow(b))
            }
        }
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val showFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    editingBook?.let { book ->
        BookBottomSheet(
            book = book,
            onDismiss = { editingBook = null },
            onUpdateRequest = { newTitle, newAuthor, newGenre, newYear, newRating ->
                coroutineScope.launch {
                    val result = snackBarHostState.showSnackbar(
                        message = "Обновить книгу?",
                        actionLabel = "Отмена",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.Dismissed) {
                        booksViewModel.updateBook(
                            id = book.id,
                            newTitle = newTitle,
                            newAuthor = newAuthor,
                            newGenre = newGenre,
                            newYear = newYear,
                            newRating = newRating
                        )
                    }
                }
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackbarData ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        if (value == SwipeToDismissBoxValue.StartToEnd) {
                            snackbarData.dismiss()
                            true
                        } else false
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = true,
                    enableDismissFromEndToStart = false,
                    backgroundContent = {}
                ) {
                    Snackbar(snackbarData)
                }
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.scrollToItem(0)
                        }
                    }
                ) {
                    Icon(Icons.Filled.ArrowUpward, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(innerPadding)
        ) {
            rows.forEach { row ->
                when (row) {
                    is Row.Header -> item { HeadLetter(row.letter, Modifier) }
                    is Row.BookRow -> item {
                        BookItem(
                            row.book,
                            onClick = { onBookClick(row.book) },
                            onLongClick = { editingBook = row.book }
                        )
                    }
                }
            }
        }
    }
}
