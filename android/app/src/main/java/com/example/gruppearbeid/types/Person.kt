package com.example.gruppearbeid.types

import java.io.Serializable

data class Person(
    val url: String,

    /** "name": "Luke Skywalker" */
    val name: String,

    val height: String,

    val mass: String,

    val hair_color: String,

    val birth_year: String,

    val gender: String,
    /** "homeworld": ["https://swapi.dev/api/planets/1/", ...] */
    val homeworld: ArrayList<String>,

    /** "films": ["https://swapi.dev/api/films/1/", ...] */
    val films: ArrayList<String>,

    /** "starships": ["https://swapi.dev/api/starships/1/", ...] */
    val starships: ArrayList<String>,

    /** "species": ["https://swapi.dev/api/species/1/", ...] */
    val species: ArrayList<String>,
) : Serializable
