package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.SpeciesListAdapter
import com.example.gruppearbeid.types.Results
import com.example.gruppearbeid.types.Species
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_species_list.*

class SpeciesListActivity : AppCompatActivity() {
    private lateinit var network: INetwork
    private val adapter = SpeciesListAdapter{ species -> navigateToThing(this, SpeciesActivity::class.java, species) }
    private var prev: String? = null
    private var next: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_species_list)
        title = getString(R.string.species)

        // 1. Init adapter
        SpeciesListRecycler.adapter = adapter
        SpeciesListRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        network = Network(this)
        val search = { search: String -> network.searchSpeciesList(search, onSuccess, onError) }
        search("")
        SpeciesListSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )

        // 3. Init pagination
        SpeciesListPrev.setOnClickListener{ prev?.let { this.network.getSpeciesList(it, onSuccess, onError) } }
        SpeciesListNext.setOnClickListener{ next?.let { this.network.getSpeciesList(it, onSuccess, onError) } }
    }

    private val onSuccess = { res: Results<Species> ->
        adapter.refresh(res.results);
        prev = res.prev
        next = res.next
        refreshPaginationViews(res, SpeciesListPrev, SpeciesListNext, SpeciesListDots)
    }

    private val onError = { err: String ->
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, SpeciesListNavigation, R.id.SpeciesMenuItem)
    }
}