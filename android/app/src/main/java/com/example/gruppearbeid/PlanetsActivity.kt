package com.example.gruppearbeid

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.databinding.ActivityPlanetsBinding
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.util.Network
import kotlinx.android.synthetic.main.activity_planets.*
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.makeTextWatcherWithDebounce
import com.example.gruppearbeid.util.navigateToThing

class PlanetsActivity : AppCompatActivity() {
    private val planets = ArrayList<Planet>()
    private lateinit var planetsXML: ActivityPlanetsBinding

    val URL = "https://image.slidesharecdn.com/7thingsstockimages-140124084729-phpapp01/95/7-types-of-stock-images-you-must-stop-using-today-40-638.jpg?cb=1390828351"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planets)
        title = getString(R.string.planets)

        planetsXML = ActivityPlanetsBinding.inflate(layoutInflater)
        val image: ImageView = findViewById<ImageView>(R.id.imagePlanets)

        Network.downloadImage(URL, image)
        // 1. Init adapter

        val adapter = PlanetsAdapter(planets){ planet ->
            navigateToThing(this, PlanetActivity::class.java, planet)
        }
        PlanetRecycler.adapter = adapter
        PlanetRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        val search = { text: String ->
            Network.getPlanets(
                search = text,
                onSuccess = { _planets ->
                    planets.clear()
                    planets.addAll(_planets)
                    adapter.notifyDataSetChanged()
                },
                onError = { error ->
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                })
        }
        search("")
        PlanetsSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PlanetsNavigation, R.id.PlanetsMenuItem)
    }
}