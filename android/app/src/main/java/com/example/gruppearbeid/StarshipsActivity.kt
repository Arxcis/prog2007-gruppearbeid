package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.StarshipsAdapter
import com.example.gruppearbeid.types.Starship
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_starships.*

class StarshipsActivity : AppCompatActivity() {
    private val starships = ArrayList<Starship>()
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starships)
        title = getString(R.string.starships)

        // 1. Init adapter
        val adapter = StarshipsAdapter(starships){ starship ->
            navigateToThing(this, StarshipActivity::class.java, starship)
        }
        StarshipRecycler.adapter = adapter
        StarshipRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        network = Network(this)
        val search = { text: String ->
            network.getStarships(
                search = text,
                onSuccess = { _starships ->
                    starships.clear()
                    starships.addAll(_starships)
                    adapter.notifyDataSetChanged()
                },
                onError = { error ->
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                })
        }
        search("")
        StarshipsSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, StarshipsNavigation, R.id.StarshipsMenuItem)
    }
}