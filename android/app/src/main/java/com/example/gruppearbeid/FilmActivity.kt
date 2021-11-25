package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.navigateToThing
import kotlinx.android.synthetic.main.activity_film.*
import kotlinx.android.synthetic.main.activity_person.*


class FilmActivity : AppCompatActivity() {
    private val characters = ArrayList<Person>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)

        // 1. Get extras
        val film = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Film
        title = film?.title ?: ""
        ActivityFilmName.text = film?.title ?: ""

        // 2. Init characters adapter
        val adapter = PeopleAdapter(characters){ character ->
            navigateToThing(this, PersonActivity::class.java, character)
        }
        ActivityFilmCharacters.adapter = adapter
        ActivityFilmCharacters.layoutManager = LinearLayoutManager(this)

        // 3. Get characters for this film, from the network
        if (film != null) {
            Network.getCharactersByFilm(film, characters, adapter){ error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}