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

    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, FilmsNavigation, R.id.FilmsMenuItem)
    }
}