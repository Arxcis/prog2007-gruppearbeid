package com.example.gruppearbeid.types

import java.io.Serializable


/** Typedef: https://swapi.dev/documentation#films */

data class Film(
    /** "title": "A New Hope" */
    val title: String,

    /** "characters": ["https://swapi.dev/api/characters/1/", ...] */
    //val characters: ArrayList<String>,
) : Serializable