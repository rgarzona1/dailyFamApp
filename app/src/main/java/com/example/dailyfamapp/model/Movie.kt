package com.example.dailyfamapp.model

data class Movie(
    val name: String,
    val type: String,
    val plot: String,
    val url: String
){

    fun getImageId(): Int {
        val parts = url.split("/")
        return parts[parts.size - 2].toInt()
    }
}

