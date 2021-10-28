package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_spaceships.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation

class SpaceshipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spaceships)

        configureBottomNavigation(this, SpaceshipsNavigation, R.id.SpaceshipsMenuItem)
    }
}