package com.example.gruppearbeid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.databinding.ActivityPlanetsBinding
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.util.Network
import kotlinx.android.synthetic.main.activity_planets.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_films.*
import kotlinx.android.synthetic.main.activity_people.*

class PlanetsActivity : AppCompatActivity() {
    private val planets = ArrayList<Planet>()
    private lateinit var planetsXML: ActivityPlanetsBinding

    val URL = "https://image.slidesharecdn.com/7thingsstockimages-140124084729-phpapp01/95/7-types-of-stock-images-you-must-stop-using-today-40-638.jpg?cb=1390828351"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planets)
        title = "Planets"

        planetsXML = ActivityPlanetsBinding.inflate(layoutInflater)

        val btn: Button = findViewById(R.id.btnForTestImage)
        btn.setOnClickListener{
            val intent = Intent(this, TestImage::class.java)
            startActivity(intent)
        }

        // Init adapter
        val adapter = PlanetsAdapter(planets){ planet ->
            navigateToThing(this, PlanetActivity::class.java, planet)
        }

        Network.getPlanets(planets, adapter){ error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        PlanetRecycler.adapter = adapter
        PlanetRecycler.layoutManager = LinearLayoutManager(this)
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PlanetsNavigation, R.id.PlanetsMenuItem)
    }
}