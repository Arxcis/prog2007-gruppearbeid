package com.example.gruppearbeid.types

import java.io.Serializable

data class Planet(
    /** "name": "Tatooine" */
    val name: String,

    val rotation_period: String,

    val orbital_period: String,

    val climate: String,

    val terrain: String,

    val population: String,
    /** "residents": ["https://swapi.dev/api/people/1/", ...] */
    val residents: ArrayList<String>,

    /** "films": ["https://swapi.dev/api/films/1/", ...] */
    val films: ArrayList<String>,
) : Serializable
