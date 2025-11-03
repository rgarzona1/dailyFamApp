package com.example.dailyfamapp
import android.app.AlertDialog
import androidx.appcompat.app.ActionBarDrawerToggle
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dailyfamapp.databinding.ActivityMainBinding
import com.example.dailyfamapp.ui.MoviesActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.dailyfamapp.R
import android.util.Log


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadSavedTheme()



        toggle= ActionBarDrawerToggle(this,binding.main,R.string.open,R.string.close)
        binding.main.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener(this)
        binding.imageButton5.setOnClickListener {
            binding.main.openDrawer(GravityCompat.START)
        }
        // Configura el botón para abrir la lista de tareas
        binding.listaButton.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }
        binding.moviesButton.setOnClickListener {
            val intent = Intent(this, MoviesActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                // Lógica para cerrar sesión
                Toast.makeText(this, "Cerrar sesión", Toast.LENGTH_SHORT).show()
        }
            R.id.action_aspecto -> {
                val temas =arrayOf("verde","azul","naranja")
                AlertDialog.Builder(this)
                    .setTitle("Selecciona un tema de color")
                    .setItems(temas) { _, which ->
                        val tema = when (which) {
                            0 -> "verde"
                            1 -> "azul"
                            2 -> "naranja"
                            else -> "verde"
                        }
                        saveThemePreference(tema)
                        applyThemeColors(tema)
                    }
                    .show()
                Toast.makeText(this, "Cambiar aspecto", Toast.LENGTH_SHORT).show()
            }

        }

        binding.main.closeDrawer(GravityCompat.START)
        return true
    }
    private fun saveThemePreference(themeName: String) {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        prefs.edit().putString("themeName", themeName).apply()
    }
    private fun loadSavedTheme() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val themeName = prefs.getString("themeName", "verde") ?: "verde"
        applyThemeColors(themeName)
    }
    private fun applyThemeColors(themeName: String) {
        val fondo: Int
        val navbar: Int
        val menu: Int

        when (themeName) {
            "verde" -> {
                fondo = ContextCompat.getColor(this, R.color.fondo_verde)
                navbar = ContextCompat.getColor(this, R.color.navbar_verde)
                menu = ContextCompat.getColor(this, R.color.menu_verde)
            }
            "azul" -> {
                fondo = ContextCompat.getColor(this, R.color.menu_azul)
                navbar = ContextCompat.getColor(this, R.color.navbar_azul)
                menu = ContextCompat.getColor(this, R.color.fondo_azul)
            }
            "naranja" -> {
                fondo = ContextCompat.getColor(this, R.color.fondo_naranja)
                navbar = ContextCompat.getColor(this, R.color.navbar_naranja)
                menu = ContextCompat.getColor(this, R.color.menu_naranja)
            }
            else -> {
                fondo = ContextCompat.getColor(this, R.color.fondo_verde)
                navbar = ContextCompat.getColor(this, R.color.navbar_verde)
                menu = ContextCompat.getColor(this, R.color.menu_verde)
            }
        }

        // Aplicar colores
        binding.constraintLayout.setBackgroundColor(fondo)
        binding.bottomNavigationView.setBackgroundColor(navbar)
        binding.navigationView.setBackgroundColor(menu)
    }


}

