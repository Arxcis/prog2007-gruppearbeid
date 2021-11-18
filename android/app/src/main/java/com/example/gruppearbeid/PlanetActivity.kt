package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Planet
import kotlinx.android.synthetic.main.activity_film.*
import kotlinx.android.synthetic.main.activity_planet.*

class PlanetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet)

        // Get extras
        val planet = intent.extras?.getSerializable("EXTRA_THING") as? Planet

        // Set activity title
        title = planet?.name ?: ""

        // Set content of activity
        ActivityPlanetName.text = planet?.name ?: ""
    }
}