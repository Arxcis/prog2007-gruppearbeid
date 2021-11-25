package com.example.gruppearbeid.types

import java.io.Serializable


/** Typedef: https://swapi.dev/documentation#films */

data class Film(
    /** "title": "A New Hope" */
    val title: String,

    /** "characters": ["https://swapi.dev/api/people/1/", ...] */
    val characters: ArrayList<String>,

    /** "planets": ["https://swapi.dev/api/planets/1/", ...] */
    val planets: ArrayList<String>,

    /** "starships": ["https://swapi.dev/api/starships/1/", ...] */
    val starships: ArrayList<String>,
) : Serializable