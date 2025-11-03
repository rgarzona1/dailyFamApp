package com.example.dailyfamapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyfamapp.BuildConfig
import com.example.dailyfamapp.R
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import android.widget.ImageButton
import com.example.dailyfamapp.data.RetrofitClient

import kotlinx.coroutines.launch

class MoviesActivity : AppCompatActivity() {

    private val apiKey = BuildConfig.OMDB_API_KEY
    private lateinit var adapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        Log.d("MoviesActivity", "API KEY -> $apiKey")

        val etBuscar = findViewById<EditText>(R.id.etBuscar)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnBack = findViewById<ImageButton>(R.id.btnBackk)
        btnBack.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerMovies)

        adapter = MovieAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Peliculas de inicio
        loadInitialMovies()

        // Buscar titulo o genero
        btnBuscar.setOnClickListener {
            val query = etBuscar.text.toString().trim()
            if (query.isNotEmpty()) {
                searchMovies(query)
            }
        }
    }

    private fun loadInitialMovies() {
        val initialList = listOf("Inception", "Avengers", "Titanic", "Interstellar", "Joker")

        lifecycleScope.launch {
            val movies = initialList.map { title ->
                async {
                    val response = RetrofitClient.instance.searchByTitle(apiKey, title)
                    response.Search?.firstOrNull()
                }
            }.awaitAll().filterNotNull()

            adapter.updateMovies(movies)
        }
    }

    private fun searchMovies(query: String) {
        lifecycleScope.launch {
            val response = RetrofitClient.instance.searchByTitle(apiKey, query)
            val results = response.Search ?: emptyList()
            adapter.updateMovies(results)
        }
    }
}
