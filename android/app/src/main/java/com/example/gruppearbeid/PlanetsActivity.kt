package com.example.gruppearbeid

import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.databinding.ActivityPlanetsBinding
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.types.Results
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_planets.*
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.makeTextWatcherWithDebounce
import com.example.gruppearbeid.util.navigateToThing
import java.util.jar.Manifest

class PlanetsActivity : AppCompatActivity() {
    private lateinit var network: INetwork
    private val adapter = PlanetsAdapter{ planet -> navigateToThing(this, PlanetActivity::class.java, planet) }
    private var prev: String? = null
    private var next: String? = null

    val URL = "https://image.slidesharecdn.com/7thingsstockimages-140124084729-phpapp01/95/7-types-of-stock-images-you-must-stop-using-today-40-638.jpg?cb=1390828351"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planets)
        title = getString(R.string.planets)

        // 1. Init adapter
        PlanetRecycler.adapter = adapter
        PlanetRecycler.layoutManager = LinearLayoutManager(this)

        network = Network(this)

        // 2. Init search
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