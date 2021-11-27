package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.adapters.SpeciesListAdapter
import com.example.gruppearbeid.adapters.StarshipsAdapter
import com.example.gruppearbeid.types.*
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.INetwork
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_person.*

class PersonActivity : AppCompatActivity() {
    private val homeworld = ArrayList<Planet>()
    private val films = ArrayList<Film>()
    private val starships = ArrayList<Starship>()
    private val speciesList = ArrayList<Species>()
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        // 1. Get extras
        val person = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Person
        title = "👨‍🦲 ${person?.name}"

        // 2. Init homeworld adapter
        val homeworldAdapter = PlanetsAdapter(homeworld){ homeworld ->
            navigateToThing(this, PlanetActivity::class.java, homeworld)
        }
        ActivityPersonHomeworld.adapter = homeworldAdapter
        ActivityPersonHomeworld.layoutManager = LinearLayoutManager(this)

        // 2. Init films adapter
        val filmsAdapter = FilmsAdapter(films){ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }
        ActivityPersonFilms.adapter = filmsAdapter
        ActivityPersonFilms.layoutManager = LinearLayoutManager(this)

        // 2. Init starships adapter
        val starshipAdapter = StarshipsAdapter(starships){ starship ->
            navigateToThing(this, StarshipActivity::class.java, starship)
        }
        ActivityPersonStarships.adapter = starshipAdapter
        ActivityPersonStarships.layoutManager = LinearLayoutManager(this)
        
        // 2. Init species adapter
        val speciesListAdapter = SpeciesListAdapter(speciesList){ species ->
            navigateToThing(this, SpeciesActivity::class.java, species)
        }
        ActivityPersonSpecies.adapter = speciesListAdapter
        ActivityPersonSpecies.layoutManager = LinearLayoutManager(this)

        // 3. Get data from network
        network = Network(this)
        if (person != null) {
            network.getPlanetsByURL(person.homeworld,
                onSuccess = { _homeworld -> homeworld.clear(); homeworld.addAll(_homeworld); homeworldAdapter.notifyDataSetChanged() },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getFilmsByURL(person.films,
                onSuccess = { _films -> films.clear(); films.addAll(_films); filmsAdapter.notifyDataSetChanged() },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getStarshipsByURL(person.starships,
                onSuccess = { _starships -> starships.clear(); starships.addAll(_starships); starshipAdapter.notifyDataSetChanged() },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getSpeciesByURL(person.species,
                onSuccess = { _species -> speciesList.clear(); speciesList.addAll(_species); speciesListAdapter.notifyDataSetChanged() },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
        }
    }
}