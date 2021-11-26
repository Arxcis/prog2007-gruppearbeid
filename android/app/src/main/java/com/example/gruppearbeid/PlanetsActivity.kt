package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.util.Network
import kotlinx.android.synthetic.main.activity_planets.*
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.makeTextWatcherWithDebounce
import com.example.gruppearbeid.util.navigateToThing

class PlanetsActivity : AppCompatActivity() {
    private val planets = ArrayList<Planet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planets)
        title = getString(R.string.planets)

        // 1. Init adapter
        val adapter = PlanetsAdapter(planets){ planet ->
            navigateToThing(this, PlanetActivity::class.java, planet)
        }
        PlanetRecycler.adapter = adapter
        PlanetRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        val search = { text: String ->
            Network.getPlanets(
                search = text,
                onSuccess = { _planets ->
                    planets.clear()
                    planets.addAll(_planets)
                    adapter.notifyDataSetChanged()
                },
                onError = { error ->
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                })
        }
        search("")
        PlanetsSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PlanetsNavigation, R.id.PlanetsMenuItem)
    }
}