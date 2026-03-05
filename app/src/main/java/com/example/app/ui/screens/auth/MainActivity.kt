package com.example.app.ui.screens.auth

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.app.data.db.BooksDatabase
import com.example.app.data.db.UserEntity
import com.example.app.ui.theme.BookAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                val db = BooksDatabase.getInstance(context)
                val dao = db.booksDao()
                val defaults = listOf(
                    UserEntity(id = 0, email = "test", password = "test"),
                    UserEntity(id = 1, email = "a", password = "a"),
                    UserEntity(id = 2, email = "b", password = "b"),
                    UserEntity(id = 3, email = "c", password = "c"),
                    UserEntity(id = 4, email = "d", password = "d"),
                    UserEntity(id = 5, email = "", password = "")
                )

                withContext(Dispatchers.IO) {
                    defaults.forEach { user ->
                        val existingUser = dao.getUser(user.email)
                        if (existingUser == null) {
                            dao.insertUser(user)
                        }
                    }
                }
            }

            BookAppTheme {
                AuthenticationForm(
                    modifier = Modifier,
                    isHorizontal = (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
                )
            }
        }
    }
}