package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_planets.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import kotlinx.android.synthetic.main.activity_films.*
import kotlinx.android.synthetic.main.activity_people.*

class PlanetsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planets)

    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PlanetsNavigation, R.id.PlanetsMenuItem)
    }
}