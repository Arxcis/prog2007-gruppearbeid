package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_planets.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation

class PlanetsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planets)

        configureBottomNavigation(this, PlanetsNavigation, R.id.PlanetsMenuItem)
    }
}