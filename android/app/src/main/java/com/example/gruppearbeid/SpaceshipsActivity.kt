package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_starships.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation

class SpaceshipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starships)

    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, StarshipsNavigation, R.id.SpaceshipsMenuItem)
    }
}