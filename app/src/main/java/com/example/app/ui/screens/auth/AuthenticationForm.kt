package com.example.app.ui.screens.auth

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.data.BooksPrefs
import com.example.app.data.db.BooksDatabase
import com.example.app.ui.screens.books.BookActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AuthenticationForm(modifier: Modifier, isHorizontal: Boolean) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Column(
        modifier
            .fillMaxSize()
            .background(Color(60, 60, 60)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.lib_logo),
            contentDescription = "LibLogo",
            modifier = modifier
                .padding(top = if (isHorizontal) 30.dp else 80.dp)
                .size(100.dp)
        )
        Column(
            modifier = modifier
                .align(Alignment.Start)
                .fillMaxWidth()
        ) {
            TextField(
                value = email,
                onValueChange = { email = it; isError = false },
                modifier = modifier
                    .fillMaxWidth(if (isHorizontal) 0.6f else 0.8f)
                    .padding(bottom = 50.dp),
                placeholder = { Text("Email") },
                isError = isError
            )
            TextField(
                value = password,
                onValueChange = { password = it; isError = false },
                modifier = modifier
                    .fillMaxWidth(if (isHorizontal) 0.6f else 0.8f),
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text("Password") },
                isError = isError
            )
        }
        Button(
            onClick = {
                scope.launch {
                    val db = BooksDatabase.getInstance(context)
                    val dao = db.booksDao()

                    val user = withContext(Dispatchers.IO) { dao.getUser(email) }

                    if (user != null && user.password == password) {
                        val prefs = BooksPrefs(context)
                        prefs.currentUserId = user.id
                        
                        context.startActivity(Intent(context, BookActivity::class.java))
                    } else {
                        isError = true
                    }
                }
            },
            modifier
                .padding(if (isHorizontal) 10.dp else 60.dp)
                .width(250.dp)
        ) {
            Text("Войти")
        }
    }
}
