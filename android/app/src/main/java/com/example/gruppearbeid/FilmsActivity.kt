package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_films.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation

class FilmsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        configureBottomNavigation(this, FilmsNavigation, R.id.FilmsMenuItem)
    }
}