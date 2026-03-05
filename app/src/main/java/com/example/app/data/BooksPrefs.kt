package com.example.app.data

import android.content.Context
import androidx.core.content.edit

class BooksPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("books_prefs", Context.MODE_PRIVATE)

    var initialized: Boolean
        get() = prefs.getBoolean("initialized", false)
        set(value) = prefs.edit { putBoolean("initialized", value) }
    
    var currentUserId: Long?
        get() {
            val id = prefs.getLong("current_user_id", -1L)
            return if (id == -1L) null else id
        }
        set(value) = prefs.edit { 
            if (value != null) {
                putLong("current_user_id", value)
            } else {
                remove("current_user_id")
            }
        }
}
