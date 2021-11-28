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
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starship)

        // 1. Get extras
        val starship = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Starship
        title = "🚀 ${starship?.name}"
        ActivityStarshipModel.text = "Model: ${starship?.model}"
        ActivityStarshipManufacturer.text = "Manufacturer: ${starship?.manufacturer}"
        ActivityStarshipLength.text = "Length: ${starship?.length} m"
        ActivityStarshipMaxAtmospheringSpeed.text = "Max atmosphering speed: ${starship?.max_atmosphering_speed} km/h"
        ActivityStarshipCrew.text = "Crew: ${starship?.crew}"
        ActivityStarshipPassengers.text = "Passengers: ${starship?.passengers}"
        ActivityStarshipStarshipClass.text = "Starship class: ${starship?.starship_class}"


        // 2. Init adapters
        val pilotsAdapter = PeopleAdapter{ pilot ->
            navigateToThing(this, PersonActivity::class.java, pilot)
        }
        ActivityStarshipPilots.adapter = pilotsAdapter
        ActivityStarshipPilots.layoutManager = LinearLayoutManager(this)

        val filmsAdapter = FilmsAdapter{ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }
        ActivityStarshipFilms.adapter = filmsAdapter
        ActivityStarshipFilms.layoutManager = LinearLayoutManager(this)

        // 3. Get data from network
        network = Network(this)
        if (starship != null) {
            network.getPeopleByURL(starship.pilots,
                onSuccess = { pilots -> pilotsAdapter.refresh(pilots) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
            network.getFilmsByURL(starship.films,
                onSuccess = { films -> filmsAdapter.refresh(films) },
                onError = {  error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
        }
    }
}