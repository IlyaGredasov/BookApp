package com.example.app.ui.screens.books

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app.domain.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookBottomSheet(
    book: Book,
    onDismiss: () -> Unit,
    onUpdateRequest: (newTitle: String, newAuthor: String, newGenre: String, newYear: String, newRating: String) -> Unit
) {
    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var genre by remember { mutableStateOf(book.genre) }
    var year by remember { mutableStateOf(book.publicationDate) }
    var rating by remember { mutableStateOf(book.rating) }

    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(Modifier.padding(16.dp)) {

            Text("Редактировать книгу", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(12.dp))

            TextField(value = title, onValueChange = { title = it }, label = { Text("Название") })
            TextField(value = author, onValueChange = { author = it }, label = { Text("Автор") })
            TextField(value = genre, onValueChange = { genre = it }, label = { Text("Жанр") })
            TextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Год публикации") })
            TextField(value = rating, onValueChange = { rating = it }, label = { Text("Рейтинг") })

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    onUpdateRequest(title, author, genre, year, rating)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Обновить")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
