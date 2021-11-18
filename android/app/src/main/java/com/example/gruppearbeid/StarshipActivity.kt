package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Starship
import kotlinx.android.synthetic.main.activity_person.*
import kotlinx.android.synthetic.main.activity_starship.*

// Class for a single starship
class StarshipActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starship)

        // Get extras
        val starship = intent.extras?.getSerializable("EXTRA_THING") as? Starship

        // Set activity title
        title = starship?.name

        // Set content of activity
        ActivityStarshipName.text = starship?.name ?: ""
        ActivityStarshipModel.text = starship?.model ?: ""
        ActivityStarshipManufacturer.text = starship?.manufacturer ?: ""
        ActivityStarshipLength.text = starship?.length ?: ""
        ActivityStarshipMaxAtmospheringSpeed.text = starship?.max_atmosphering_speed ?: ""
        ActivityStarshipCrew.text = starship?.crew ?: ""
        ActivityStarshipPassengers.text = starship?.passengers ?: ""
        ActivityStarshipStarshipClass.text = starship?.starship_class ?: ""
    }
}