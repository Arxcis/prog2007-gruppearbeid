package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_film.*
import kotlinx.android.synthetic.main.activity_planet.*

class PlanetActivity : AppCompatActivity() {
    private val residents = ArrayList<Person>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet)

        // 1. Get extras
        val planet = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Planet
        title = planet?.name ?: ""
        ActivityPlanetName.text = planet?.name ?: ""

        // 2. Init residents adapter
        val adapter = PeopleAdapter(residents){ character ->
            navigateToThing(this, PersonActivity::class.java, character)
        }
        ActivityPlanetResidents.adapter = adapter
        ActivityPlanetResidents.layoutManager = LinearLayoutManager(this)

        // 3. Get characters for this film, from the network
        if (planet != null) {
            Network.getPeopleByURL(planet.residents, residents, adapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}