package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Starship
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.INetwork
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_starship.*

// Class for a single starship
class StarshipActivity : AppCompatActivity() {
    val films = ArrayList<Film>()
    val pilots = ArrayList<Person>()
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starship)

        // 1. Get extras
        val starship = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Starship
        title = "🚀 ${starship?.name}"
        ActivityStarshipModel.text = starship?.model ?: ""
        ActivityStarshipManufacturer.text = starship?.manufacturer ?: ""
        ActivityStarshipLength.text = starship?.length ?: ""
        ActivityStarshipMaxAtmospheringSpeed.text = starship?.max_atmosphering_speed ?: ""
        ActivityStarshipCrew.text = starship?.crew ?: ""
        ActivityStarshipPassengers.text = starship?.passengers ?: ""
        ActivityStarshipStarshipClass.text = starship?.starship_class ?: ""

        // 2. Init adapters
        val pilotsAdapter = PeopleAdapter(pilots){ character ->
            navigateToThing(this, PersonActivity::class.java, character)
        }
        ActivityStarshipPilots.adapter = pilotsAdapter
        ActivityStarshipPilots.layoutManager = LinearLayoutManager(this)

        val filmsAdapter = FilmsAdapter(films){ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }
        ActivityStarshipFilms.adapter = filmsAdapter
        ActivityStarshipFilms.layoutManager = LinearLayoutManager(this)

        // 3. Get data from network
        network = Network(this)
        if (starship != null) {
            network.getPeopleByURL(starship.pilots, pilots, pilotsAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            network.getFilmsByURL(starship.films, films, filmsAdapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}