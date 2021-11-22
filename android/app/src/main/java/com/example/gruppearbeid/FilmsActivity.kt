package com.example.gruppearbeid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.util.Network
import kotlinx.android.synthetic.main.activity_films.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.navigateToThing

class FilmsActivity : AppCompatActivity() {
    private val films = ArrayList<Film>()

    private lateinit var adapter: FilmsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)
        title = "Films"

        // Init adapter
        adapter = FilmsAdapter(films){ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }
        fetchFilms()

        FilmsRecycler.adapter = adapter
        FilmsRecycler.layoutManager = LinearLayoutManager(this)
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, FilmsNavigation, R.id.FilmsMenuItem)
    }

    fun fetchFilms()
    {
        Network.getFilms(films, adapter){ error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
}