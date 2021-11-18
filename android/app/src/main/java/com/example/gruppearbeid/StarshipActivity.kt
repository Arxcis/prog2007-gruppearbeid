package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Starship
import com.example.gruppearbeid.util.Constants
import kotlinx.android.synthetic.main.activity_person.*
import kotlinx.android.synthetic.main.activity_starship.*

class StarshipActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starship)

        // Get extras
        val starship = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Starship

        // Set activity title
        title = starship?.name

        // Set content of activity
        ActivityStarshipName.text = starship?.name ?: ""
    }
}