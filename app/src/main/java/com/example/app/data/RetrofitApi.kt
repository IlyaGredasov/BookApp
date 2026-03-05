package com.example.app.data

import android.util.Log
import com.example.app.data.dto.AuthorDto
import com.example.app.data.dto.BookDto
import com.example.app.data.dto.RootDto
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

fun parseRoot(json: String): RootDto {
    val root = JsonParser.parseString(json).asJsonObject

    val booksJson = root.getAsJsonArray("books") ?: JsonArray()
    val books = ArrayList<BookDto>(booksJson.size())
    for (el in booksJson) {
        val obj = el.asJsonObject
        val authorId = obj.get("AuthorId")?.asString ?: ""
        val name = obj.get("Name")?.asString ?: ""
        val rating = obj.get("rating")?.asInt ?: 0

        val pubEl = obj.get("PublicationDate")
        val publicationDate = if (pubEl == null || pubEl.isJsonNull) null else pubEl.asString

        val genreArr = obj.getAsJsonArray("Genre") ?: JsonArray()
        val genres = buildList {
            for (g in genreArr) add(g.asString)
        }

        Log.d("BOOKS", "\"AuthorId\": \"$authorId\"")
        Log.d("BOOKS", "\"Name\": \"$name\"")
        Log.d("BOOKS", "\"PublicationDate\": ${publicationDate?.let { "\"$it\"" } ?: "null"}")
        Log.d("BOOKS", "\"rating\": $rating")
        genres.forEachIndexed { i, g -> Log.d("BOOKS", "\"Genre[$i]\": \"$g\"") }

        books += BookDto(authorId, name, genres, publicationDate, rating)
    }

    val authorsJson = root.getAsJsonArray("authors") ?: JsonArray()
    val authors = ArrayList<AuthorDto>(authorsJson.size())
    for (el in authorsJson) {
        val obj = el.asJsonObject
        val id = obj.get("id")?.asString ?: ""
        val author = obj.get("Author")?.asString ?: ""
        Log.d("AUTHORS", "\"id\": \"$id\"")
        Log.d("AUTHORS", "\"Author\": \"$author\"")
        authors += AuthorDto(id, author)
    }

    return RootDto(books, authors)
}

interface BooksApi {
    @GET("books2025.json")
    suspend fun getRoot(): String
}

object Net {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/Lpirskaya/JsonLab/master/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    val api: BooksApi = retrofit.create(BooksApi::class.java)
}
