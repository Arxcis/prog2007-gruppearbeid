package com.example.gruppearbeid.types

import java.io.Serializable

data class Starship(
    /** "name": "Death Star" */
    val name: String,
    /** "model": "DS-1 Orbital Battle Station" **/
    val model: String,

    val manufacturer: String,

    val length: String,

    val max_atmosphering_speed: String,

    val crew: String,

    val passengers: String,

    val starship_class: String,

    /** "films": ["https://swapi.dev/api/films/1/", ...] */
    val films: ArrayList<String>,

    /** "pilots": ["https://swapi.dev/api/people/1/", ...] */
    val pilots: ArrayList<String>,
    ) : Serializable
