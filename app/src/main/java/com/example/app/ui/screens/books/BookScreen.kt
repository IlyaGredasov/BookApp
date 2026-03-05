package com.example.app.ui.screens.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.app.domain.Book

@Composable
fun BookScreen(book: Book) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = book.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )

        Column(
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Автор: ${book.author}")
            Spacer(Modifier.height(8.dp))
            Text("Жанр: ${book.genre}")
            Spacer(Modifier.height(8.dp))
            Text("Год публикации: ${book.publicationDate.ifBlank { "-" }}")
            Spacer(Modifier.height(8.dp))
            Text("Рейтинг: ${book.rating}")
        }
    }
}
