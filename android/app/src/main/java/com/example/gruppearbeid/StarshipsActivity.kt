package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.StarshipsAdapter
import com.example.gruppearbeid.types.Results
import com.example.gruppearbeid.types.Starship
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_starships.*

class StarshipsActivity : AppCompatActivity() {
    private lateinit var network: INetwork
    val adapter = StarshipsAdapter{ starship -> navigateToThing(this, StarshipActivity::class.java, starship) }
    private var prev: String? = null
    private var next: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starships)
        title = getString(R.string.starships)

        // 1. Init adapter
        StarshipRecycler.adapter = adapter
        StarshipRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        network = Network(this)
        val search = { search: String -> network.searchStarships(search, onSuccess, onError) }
        search("")
        StarshipsSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )

        // 3. Init pagination
        StarshipsPrev.setOnClickListener{ prev?.let { this.network.getStarships(it, onSuccess, onError) } }
        StarshipsNext.setOnClickListener{ next?.let { this.network.getStarships(it, onSuccess, onError) } }
    }

    private val onSuccess = { res: Results<Starship> ->
        adapter.refresh(res.results);
        prev = res.prev
        next = res.next
        refreshPaginationViews(res, StarshipsPrev, StarshipsNext, StarshipsDots)
    }

    private val onError = { err: String ->
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, StarshipsNavigation, R.id.StarshipsMenuItem)
    }
}