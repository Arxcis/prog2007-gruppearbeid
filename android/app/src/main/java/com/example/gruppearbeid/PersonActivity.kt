package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_film.*
import kotlinx.android.synthetic.main.activity_films.*
import kotlinx.android.synthetic.main.activity_person.*

class PersonActivity : AppCompatActivity() {
    private val films = ArrayList<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        // 1. Get extras
        val person = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Person
        title = person?.name
        ActivityPersonName.text = person?.name ?: ""

        // 2. Init films adapter
        val adapter = FilmsAdapter(films){ film ->
            navigateToThing(this, FilmActivity::class.java, film)
        }
        ActivityPersonFilms.adapter = adapter
        ActivityPersonFilms.layoutManager = LinearLayoutManager(this)

        // 3. Get films of this person, from the network
        if (person != null) {
            Network.getFilmsByURL(person.films, films, adapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}