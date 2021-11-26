package com.example.gruppearbeid.types

import java.io.Serializable

data class Species(
    /** "name": "Human" */
    val name: String,

    /** "people": ["https://swapi.dev/api/people/1/", ...] */
    val people: ArrayList<String>,

    /** "films": ["https://swapi.dev/api/films/1/", ...] */
    val films: ArrayList<String>,
    ) : Serializable
