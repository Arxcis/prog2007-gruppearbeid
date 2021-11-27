package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.SpeciesListAdapter
import com.example.gruppearbeid.types.Species
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_species_list.*

class SpeciesListActivity : AppCompatActivity() {
    private val speciesList = ArrayList<Species>()
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_species_list)
        title = getString(R.string.species)

        // 1. Init adapter
        val adapter = SpeciesListAdapter(speciesList){ starship ->
            navigateToThing(this, SpeciesActivity::class.java, starship)
        }
        SpeciesListRecycler.adapter = adapter
        SpeciesListRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        network = Network(this)
        val search = { text: String ->
            network.searchSpeciesList(
                search = text,
                onSuccess = { res -> speciesList.clear(); speciesList.addAll(res.results); adapter.notifyDataSetChanged() },
                onError = { error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
        }
        search("")
        SpeciesListSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, SpeciesListNavigation, R.id.SpeciesMenuItem)
    }
}