package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.view.menu.MenuView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.adapters.SpeciesListAdapter
import com.example.gruppearbeid.adapters.StarshipsAdapter
import com.example.gruppearbeid.types.*
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_person.*

class PersonActivity : AppCompatActivity() {
    private lateinit var network: INetwork

    private val viewModel: ItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        // 1. Get extras
        val person = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Person
        title = "👨‍🦲 ${person?.name}"

        viewModel.selectItem(person?.url ?: "")

        // 2. Init homeworld adapter
        val homeworldAdapter = PlanetsAdapter{ homeworld ->
            navigateToThing(this, PlanetActivity::class.java, homeworld)
        }
        ActivityPersonHomeworld.adapter = homeworldAdapter
        ActivityPersonHomeworld.layoutManager = LinearLayoutManager(this)

        // 2. Init films adapter
        val filmsAdapter = FilmsAdapter{ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }
        ActivityPersonFilms.adapter = filmsAdapter
        ActivityPersonFilms.layoutManager = LinearLayoutManager(this)

        // 2. Init starships adapter
        val starshipAdapter = StarshipsAdapter{ starship ->
            navigateToThing(this, StarshipActivity::class.java, starship)
        }
        ActivityPersonStarships.adapter = starshipAdapter
        ActivityPersonStarships.layoutManager = LinearLayoutManager(this)
        
        // 2. Init species adapter
        val speciesListAdapter = SpeciesListAdapter{ species ->
            navigateToThing(this, SpeciesActivity::class.java, species)
        }
        ActivityPersonSpecies.adapter = speciesListAdapter
        ActivityPersonSpecies.layoutManager = LinearLayoutManager(this)

        // 3. Get data from network
        network = Network(this)
        if (person != null) {
            network.getPlanetsByURL(person.homeworld,
                onSuccess = { homeworld -> homeworldAdapter.refresh(homeworld) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getFilmsByURL(person.films,
                onSuccess = { films -> filmsAdapter.refresh(films) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getStarshipsByURL(person.starships,
                onSuccess = { starships -> starshipAdapter.refresh(starships) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getSpeciesByURL(person.species,
                onSuccess = { species -> speciesListAdapter.refresh(species) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
        }
    }
}