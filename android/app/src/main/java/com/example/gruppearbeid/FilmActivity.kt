package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.adapters.SpeciesListAdapter
import com.example.gruppearbeid.adapters.StarshipsAdapter
import com.example.gruppearbeid.types.*
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.INetwork
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_film.*


class FilmActivity : AppCompatActivity() {
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)

        // 1. Get extras
        val film = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Film
        title = "🎬 ${film?.title}"
        ActivityFilmName.text = "Title: ${film?.title}"
        ActivityFilmIdNumber.text = "Number: ${film?.episode_id}"
        ActivityFilmDirector.text = "Director: ${film?.director}"
        ActivityFilmProducer.text = "Producer: ${film?.producer}"
        ActivityFilmReleaseDate.text = "Release date: ${film?.release_date}"


        // 2. Init characters adapter
        val charactersAdapter = PeopleAdapter{ character ->
            navigateToThing(this, PersonActivity::class.java, character)
        }
        ActivityFilmCharacters.adapter = charactersAdapter
        ActivityFilmCharacters.layoutManager = LinearLayoutManager(this)

        // 2. Init planets adapter
        val planetsAdapter = PlanetsAdapter{ planet ->
            navigateToThing(this, PlanetActivity::class.java, planet)
        }
        ActivityFilmPlanets.adapter = planetsAdapter
        ActivityFilmPlanets.layoutManager = LinearLayoutManager(this)

        // 2. Init starships adapter
        val starshipAdapter = StarshipsAdapter{ starship ->
            navigateToThing(this, StarshipActivity::class.java, starship)
        }
        ActivityFilmStarships.adapter = starshipAdapter
        ActivityFilmStarships.layoutManager = LinearLayoutManager(this)

        // 2. Init species adapter
        val speciesListAdapter = SpeciesListAdapter{ species ->
            navigateToThing(this, SpeciesActivity::class.java, species)
        }
        ActivityFilmSpecies.adapter = speciesListAdapter
        ActivityFilmSpecies.layoutManager = LinearLayoutManager(this)

        // 3. Get data from network
        network = Network(this)
        if (film != null) {
            network.getPeopleByURL(film.characters,
                onSuccess = { characters -> charactersAdapter.refresh(characters) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getPlanetsByURL(film.planets,
                onSuccess = { planets -> planetsAdapter.refresh(planets) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getStarshipsByURL(film.starships,
                onSuccess = { starships -> starshipAdapter.refresh(starships) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getSpeciesByURL(film.species,
                onSuccess = { species -> speciesListAdapter.refresh(species) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
        }
    }
}