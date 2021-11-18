package com.example.gruppearbeid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import kotlinx.android.synthetic.main.activity_films.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.navigateToThing

class FilmsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)
        title = "Films"

        // Init adapter
        val adapter = FilmsAdapter(){ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }

        FilmsRecycler.adapter = adapter
        FilmsRecycler.layoutManager = LinearLayoutManager(this)
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, FilmsNavigation, R.id.FilmsMenuItem)
    }
}