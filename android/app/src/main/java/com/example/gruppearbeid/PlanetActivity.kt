package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.INetwork
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_planet.*

class PlanetActivity : AppCompatActivity() {
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet)

        // 1. Get extras
        val planet = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Planet
        title = "🪐 ${planet?.name}"
        ActivityPlanetRotationPeriod.text = "Rotation period: ${planet?.rotation_period} standard hours"
        ActivityPlanetOrbitalPeriod.text = "Orbital period: ${planet?.orbital_period} local days"
        ActivityPlanetClimate.text = "Climate: ${planet?.climate}"
        ActivityPlanetTerrain.text = "Terrain: ${planet?.terrain}"
        ActivityPlanetPopulation.text = "Population: ${planet?.population}"

        // 2. Init residents adapter
        val residentsAdapter = PeopleAdapter{ resident ->
            navigateToThing(this, PersonActivity::class.java, resident)
        }
        ActivityPlanetResidents.adapter = residentsAdapter
        ActivityPlanetResidents.layoutManager = LinearLayoutManager(this)

        // 2. Init films adapter
        val filmsAdapter = FilmsAdapter{ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }
        ActivityPlanetFilms.adapter = filmsAdapter
        ActivityPlanetFilms.layoutManager = LinearLayoutManager(this)

        // 3. Get data from network
        network = Network(this)
        if (planet != null) {
            network.getPeopleByURL(planet.residents,
                onSuccess = { residents -> residentsAdapter.refresh(residents) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getFilmsByURL(planet.films,
                onSuccess = { films -> filmsAdapter.refresh(films) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
        }
    }
}