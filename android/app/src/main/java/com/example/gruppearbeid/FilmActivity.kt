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
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_film.*


class FilmActivity : AppCompatActivity() {
    private val characters = ArrayList<Person>()
    private val planets = ArrayList<Planet>()
    private val starships = ArrayList<Starship>()
    private val speciesList = ArrayList<Species>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)

        // 1. Get extras
        val film = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Film
        title = "🎬 ${film?.title}"

        // 2. Init characters adapter
        val charactersAdapter = PeopleAdapter(characters){ character ->
            navigateToThing(this, PersonActivity::class.java, character)
        }
        ActivityFilmCharacters.adapter = charactersAdapter
        ActivityFilmCharacters.layoutManager = LinearLayoutManager(this)

        // 2. Init planets adapter
        val planetsAdapter = PlanetsAdapter(planets){ planet ->
            navigateToThing(this, PlanetActivity::class.java, planet)
        }
        ActivityFilmPlanets.adapter = planetsAdapter
        ActivityFilmPlanets.layoutManager = LinearLayoutManager(this)

        // 2. Init starships adapter
        val starshipAdapter = StarshipsAdapter(starships){ starship ->
            navigateToThing(this, StarshipActivity::class.java, starship)
        }
        ActivityFilmStarships.adapter = starshipAdapter
        ActivityFilmStarships.layoutManager = LinearLayoutManager(this)

        // 2. Init species adapter
        val speciesListAdapter = SpeciesListAdapter(speciesList){ species ->
            navigateToThing(this, SpeciesActivity::class.java, species)
        }
        ActivityFilmSpecies.adapter = speciesListAdapter
        ActivityFilmSpecies.layoutManager = LinearLayoutManager(this)

        // 3. Get data from network
        if (film != null) {
            Network.getPeopleByURL(film.characters, characters, charactersAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            Network.getPlanetsByURL(film.planets, planets, planetsAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            Network.getStarshipsByURL(film.starships, starships, starshipAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            Network.getSpeciesByURL(film.species, speciesList, speciesListAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}