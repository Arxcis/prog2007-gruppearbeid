package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.util.Constants
import kotlinx.android.synthetic.main.activity_film.*


class FilmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)

        // Get extras
        val film = intent.extras?.getSerializable(Constants.EXTRA_THING) as? Film

        // Set activity title
        title = "🎬 ${film?.title}"

        // Set content of activity
        ActivityFilmName.text = film?.title ?: ""
    }
}