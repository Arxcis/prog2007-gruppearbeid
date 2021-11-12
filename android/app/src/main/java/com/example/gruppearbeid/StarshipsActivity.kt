package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.StarshipsAdapter
import kotlinx.android.synthetic.main.activity_starships.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import kotlinx.android.synthetic.main.activity_planets.*

class StarshipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starships)
        title = "SWAPI / Starships"

        // Init adapter
        val adapter = StarshipsAdapter()
        StarshipRecycler.adapter = adapter
        StarshipRecycler.layoutManager = LinearLayoutManager(this)
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, StarshipsNavigation, R.id.StarshipsMenuItem)
    }
}