package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Results
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_people.*


class PeopleActivity : AppCompatActivity() {
    private lateinit var network: INetwork
    private val adapter = PeopleAdapter{ person -> navigateToThing(this, PersonActivity::class.java, person) }
    private var prev: String? = null
    private var next: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)
        title = getString(R.string.people)

        // 1. Init adapter
        PeopleRecycler.adapter = adapter
        PeopleRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        network = Network(this)
        val search = { search: String -> network.searchPeople(search, onSuccess, onError) }
        search("")
        PeopleSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )

        // 3. Init pagination
        PeoplePrev.setOnClickListener{ prev?.let { this.network.getPeople(it, onSuccess, onError) } }
        PeopleNext.setOnClickListener{ next?.let { this.network.getPeople(it, onSuccess, onError) } }
    }

    private val onSuccess = { res: Results<Person> ->
        adapter.refresh(res.results);
        prev = res.prev
        next = res.next
        refreshPaginationViews(res, PeoplePrev, PeopleNext, PeopleDots)
    }

    private val onError = { err: String ->
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PeopleNavigation, R.id.PeopleMenuItem)
    }
}

