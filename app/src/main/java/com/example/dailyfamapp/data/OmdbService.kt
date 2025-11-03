package com.example.dailyfamapp.data

import retrofit2.http.GET
import retrofit2.http.Query

data class MovieResponse(
    val Search: List<Movie>?,
    val Title: String?,
    val Year: String?,
    val Genre: String?,
    val Plot: String?,
    val Poster: String?,
    val Response: String
)

data class Movie(
    val Title: String,
    val Year: String,
    val Genre: String?,
    val Plot: String?,
    val Poster: String
)

interface OmdbService {
    @GET("/")
    suspend fun searchByTitle(
        @Query("apikey") apiKey: String,
        @Query("s") title: String
    ): MovieResponse

    @GET("/")
    suspend fun searchByGenre(
        @Query("apikey") apiKey: String,
        @Query("type") type: String = "movie",
        @Query("genre") genre: String
    ): MovieResponse
}
