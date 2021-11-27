package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.types.Pagination
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Results
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_people.*


class PeopleActivity : AppCompatActivity() {
    private lateinit var network: INetwork
    private var prev: String? = null
    private var next: String? = null
    private val adapter = PeopleAdapter{ person -> navigateToThing(this, PersonActivity::class.java, person) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)
        title = getString(R.string.people)

        // 1. Init adapter
        PeopleRecycler.adapter = adapter
        PeopleRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        network = Network(this)
        val search = { text: String -> network.searchPeople(text, onSuccess, onError) }
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
        refreshPagination(res)
    }

    private val onError = { err: String ->
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
    }

    private fun refreshPagination(pag: Pagination) {
        prev = pag.prev
        next = pag.next
        Log.d("refreshPagination", "${pag.page},${pag.count},${pag.pageCount}")
        when {
            prev != null -> PeoplePrev.visibility = View.VISIBLE
            else -> PeoplePrev.visibility = View.INVISIBLE
        }
        when {
            next != null -> PeopleNext.visibility = View.VISIBLE
            else -> PeopleNext.visibility = View.INVISIBLE
        }
        var dots = ""
        for (i in 1..pag.pageCount) {
            dots = when (i){
                pag.page -> dots.plus(Constants.DOT_BIG).plus(" ")
                else -> dots.plus(Constants.DOT_SMALL).plus(" ")
            }
        }
        PeopleDots.text = dots
    }

    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PeopleNavigation, R.id.PeopleMenuItem)
    }
}
