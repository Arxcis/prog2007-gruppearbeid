package com.example.gruppearbeid.types

import java.io.Serializable


/** Typedef: https://swapi.dev/documentation#films */

data class Film(
    /** "title": "A New Hope" */
    val title: String,

    val epsiode_id: Int,

    val director: String,

    val producer: String,

    val release_date: String,

    val characters: String,
) : Serializable