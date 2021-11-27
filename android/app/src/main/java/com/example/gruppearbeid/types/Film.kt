package com.example.gruppearbeid.types

import java.io.Serializable


/** Typedef: https://swapi.dev/documentation#films */

data class Film(
    /** "title": "A New Hope" */
    override val name: String,

    val episode_id: Int,

    val director: String,

    val producer: String,

    val release_date: String,

    /** "characters": ["https://swapi.dev/api/people/1/", ...] */
    val characters: ArrayList<String>,

    /** "planets": ["https://swapi.dev/api/planets/1/", ...] */
    val planets: ArrayList<String>,

    /** "starships": ["https://swapi.dev/api/starships/1/", ...] */
    val starships: ArrayList<String>,
    
    /** "species": ["https://swapi.dev/api/species/1/", ...] */
    val species: ArrayList<String>,
) : Serializable, Type