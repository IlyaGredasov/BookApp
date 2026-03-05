package com.example.app.ui.screens.books

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.app.R
import com.example.app.domain.Book

object BookNotificationHelper {

    private const val CHANNEL_ID = "book_updates_channel"
    private const val CHANNEL_NAME = "Обновления книг"

    fun showBookUpdatedNotification(context: Context, book: Book) {
        createChannelIfNeeded(context)

        val intent = Intent(context, BookActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("fromNotification", true)
            putExtra("id", book.id)
            putExtra("title", book.title)
            putExtra("author", book.author)
            putExtra("genre", book.genre)
            putExtra("year", book.publicationDate)
            putExtra("rating", book.rating)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            book.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Книга обновлена")
            .setContentText(book.title)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Посмотреть",
                pendingIntent
            )
            .build()

        NotificationManagerCompat.from(context).notify(book.id.toInt(), notification)
    }

    private fun createChannelIfNeeded(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val existing = manager.getNotificationChannel(CHANNEL_ID)
            if (existing == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Уведомления об обновлении информации о книгах"
                }
                manager.createNotificationChannel(channel)
            }
        }
    }
}


