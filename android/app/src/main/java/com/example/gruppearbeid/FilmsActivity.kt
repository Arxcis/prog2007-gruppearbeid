package com.example.gruppearbeid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.currentActivity
import com.example.gruppearbeid.util.Network
import kotlinx.android.synthetic.main.activity_films.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.navigateToThing

class FilmsActivity : AppCompatActivity() {
    private val films = ArrayList<Film>()

    companion object {
        @JvmStatic
        val reference = this
    }

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
        currentActivity.activity = title.toString()
        configureBottomNavigation(this, FilmsNavigation, R.id.FilmsMenuItem)
    }

    override fun onPause() {
        super.onPause()
        currentActivity.activity = null
    }

    override fun onDestroy() {
        super.onDestroy()
        currentActivity.activity = null
    }

    fun fetchFilms()
    {
        films.clear()    //prevent refresh button to display data twice.
        Network.getFilms(films, adapter){ error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
}