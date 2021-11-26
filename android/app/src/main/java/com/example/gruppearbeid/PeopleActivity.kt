package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.util.Network
import kotlinx.android.synthetic.main.activity_people.*
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.makeTextWatcherWithDebounce
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_films.*


class PeopleActivity : AppCompatActivity() {
    private val people = ArrayList<Person>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)
        title = getString(R.string.people)

        // 1. Init adapter
        val adapter = PeopleAdapter(people){ person ->
            navigateToThing(this, PersonActivity::class.java, person)
        }
        PeopleRecycler.adapter = adapter
        PeopleRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        val search = { text: String ->
            Network.getPeople(
                search = text,
                onSuccess = { _people ->
                    people.clear()
                    people.addAll(_people)
                    adapter.notifyDataSetChanged()
                },
                onError = { error ->
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                })
        }
        search("")
        PeopleSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PeopleNavigation, R.id.PeopleMenuItem)
    }
}
