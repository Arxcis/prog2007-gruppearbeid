package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Species
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.INetwork
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_species.*

class SpeciesActivity : AppCompatActivity() {
    private val people = ArrayList<Person>()
    private val films = ArrayList<Film>()
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_species)

        // 1. Get extras
        val species = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Species
        title = "🧬 ${species?.name}"

        // 2. Init homeworld adapter
        val peopleAdapter = PeopleAdapter(people){ person ->
            navigateToThing(this, PersonActivity::class.java, person)
        }
        ActivitySpeciesPeople.adapter = peopleAdapter
        ActivitySpeciesPeople.layoutManager = LinearLayoutManager(this)

        // 2. Init films adapter
        val filmsAdapter = FilmsAdapter(films){ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }
        ActivitySpeciesFilms.adapter = filmsAdapter
        ActivitySpeciesFilms.layoutManager = LinearLayoutManager(this)

        // 3. Get data from network
        network = Network(this)
        if (species != null) {
            network.getPeopleByURL(species.people, people, peopleAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            network.getFilmsByURL(species.films, films, filmsAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}