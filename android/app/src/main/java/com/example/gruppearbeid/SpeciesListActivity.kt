package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.SpeciesListAdapter
import com.example.gruppearbeid.types.Species
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.makeTextWatcherWithDebounce
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_species_list.*

class SpeciesListActivity : AppCompatActivity() {
    private val speciesList = ArrayList<Species>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_species_list)
        title = getString(R.string.species)

        // 1. Init adapter
        val adapter = SpeciesListAdapter(speciesList){ starship ->
            navigateToThing(this, SpeciesListActivity::class.java, starship)
        }
        SpeciesListRecycler.adapter = adapter
        SpeciesListRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        val search = { text: String ->
            Network.getSpeciesList(
                search = text,
                onSuccess = { _speciesList ->
                    speciesList.clear()
                    speciesList.addAll(_speciesList)
                    adapter.notifyDataSetChanged()
                },
                onError = { error ->
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                })
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