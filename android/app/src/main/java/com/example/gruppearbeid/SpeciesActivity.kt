package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Species
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.INetwork
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_species.*

class SpeciesActivity : AppCompatActivity() {
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_species)

        // 1. Get extras
        val species = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Species
        title = "🧬 ${species?.name}"
        ActivitySpeciesClassification.text = "Classification: ${species?.classification}"
        ActivitySpeciesDesignation.text = "Designation: ${species?.designation}"
        ActivitySpeciesAverageHeight.text = "Average height: ${species?.average_height} cm"
        ActivitySpeciesAverageLifespan.text = "Average lifespan: ${species?.average_lifespan} years"
        ActivitySpeciesLanguage.text = "Language: ${species?.language}"

        // 2. Init homeworld adapter
        val homeworldAdapter = PlanetsAdapter{ homeworld ->
            navigateToThing(this, PlanetActivity::class.java, homeworld)
        }
        ActivitySpeciesHomeworld.adapter = homeworldAdapter
        ActivitySpeciesHomeworld.layoutManager = LinearLayoutManager(this)

        // 2. Init people adapter
        val peopleAdapter = PeopleAdapter{ person ->
            navigateToThing(this, PersonActivity::class.java, person)
        }
        ActivitySpeciesPeople.adapter = peopleAdapter
        ActivitySpeciesPeople.layoutManager = LinearLayoutManager(this)

        // 2. Init films adapter
        val filmsAdapter = FilmsAdapter{ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }
        ActivitySpeciesFilms.adapter = filmsAdapter
        ActivitySpeciesFilms.layoutManager = LinearLayoutManager(this)



        // 3. Get data from network
        network = Network(this)
        if (species != null) {
            network.getPlanetsByURL(species.homeworld,
                onSuccess = { homeworld -> homeworldAdapter.refresh(homeworld) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getPeopleByURL(species.people,
                onSuccess = { people -> peopleAdapter.refresh(people) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getFilmsByURL(species.films,
                onSuccess = { films -> filmsAdapter.refresh(films) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
        }
    }
}