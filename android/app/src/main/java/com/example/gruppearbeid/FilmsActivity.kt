package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_films.*
import kotlin.collections.ArrayList


class FilmsActivity : AppCompatActivity() {
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)
        title = getString(R.string.films)

        // 1. Init  adapter
        val adapter = FilmsAdapter{ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }
        FilmsRecycler.adapter = adapter
        FilmsRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        network = Network(this)
        val search = { text: String ->
            network.searchFilms(
                search = text,
                onSuccess = { res -> adapter.refresh(res.results) },
                onError = { error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
        }
        search("")
        FilmsSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, FilmsNavigation, R.id.FilmsMenuItem)
    }

}

