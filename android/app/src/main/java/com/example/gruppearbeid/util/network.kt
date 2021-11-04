package com.example.gruppearbeid.util

import android.os.Handler
import android.os.Looper
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.types.Starship
import java.util.concurrent.Executors

object Network {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    fun getFilms(): ArrayList<Film> = ArrayList<Film>()
    fun getPeople(): ArrayList<Person> = ArrayList<Person>()
    fun getPlanets(): ArrayList<Planet> = ArrayList<Planet>()
    fun getStarships(): ArrayList<Starship> = ArrayList<Starship>()
}