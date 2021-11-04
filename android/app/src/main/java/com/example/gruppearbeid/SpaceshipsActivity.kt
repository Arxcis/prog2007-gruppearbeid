package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_spaceships.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import kotlinx.android.synthetic.main.activity_films.*
import kotlinx.android.synthetic.main.activity_planets.*

class SpaceshipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spaceships)

    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, SpaceshipsNavigation, R.id.SpaceshipsMenuItem)
    }
}