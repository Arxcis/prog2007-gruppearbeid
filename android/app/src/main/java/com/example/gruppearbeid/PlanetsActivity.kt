package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.types.Results
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_planets.*

class PlanetsActivity : AppCompatActivity() {
    private lateinit var network: INetwork
    private val adapter = PlanetsAdapter{ planet -> navigateToThing(this, PlanetActivity::class.java, planet) }
    private var prev: String? = null
    private var next: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planets)
        title = getString(R.string.planets)

        // 1. Init adapter
        PlanetRecycler.adapter = adapter
        PlanetRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        network = Network(this)
        val search = { search: String -> network.searchPlanets(search, onSuccess, onError) }
        search("")
        PlanetsSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )

        // 3. Init pagination
        PlanetsPrev.setOnClickListener{ prev?.let { this.network.getPlanets(it, onSuccess, onError) } }
        PlanetsNext.setOnClickListener{ next?.let { this.network.getPlanets(it, onSuccess, onError) } }
    }

    private val onSuccess = { res: Results<Planet> ->
        adapter.refresh(res.results);
        prev = res.prev
        next = res.next
        refreshPaginationViews(res, PlanetsPrev, PlanetsNext, PlanetsDots)
    }

    private val onError = { err: String ->
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PlanetsNavigation, R.id.PlanetsMenuItem)
    }
}