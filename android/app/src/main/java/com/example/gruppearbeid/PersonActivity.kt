package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.adapters.StarshipsAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.types.Starship
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_person.*

class PersonActivity : AppCompatActivity() {
    private val homeworld = ArrayList<Planet>()
    private val films = ArrayList<Film>()
    private val starships = ArrayList<Starship>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        // 1. Get extras
        val person = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Person
        title = "👨‍🦲 ${person?.name}"
        ActivityPersonName.text = person?.name ?: ""

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

        // 3. Get data from network
        if (person != null) {
            Network.getPlanetsByUrl(person.homeworld, homeworld, homeworldAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            Network.getFilmsByURL(person.films, films, filmsAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            Network.getStarshipsByURL(person.starships, starships, starshipAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}