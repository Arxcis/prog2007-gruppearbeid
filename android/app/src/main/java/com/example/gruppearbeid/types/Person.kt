package com.example.gruppearbeid.types

import java.io.Serializable

data class Person(
    /** "name": "Luke Skywalker" */
    val name: String,

    /** "homeworld": ["https://swapi.dev/api/planet/1/", ...] */
    val homeworld: ArrayList<String>,

    /** "films": ["https://swapi.dev/api/films/1/", ...] */
    val films: ArrayList<String>,

    /** "starships": ["https://swapi.dev/api/starships/1/", ...] */
    val starships: ArrayList<String>,
) : Serializable
