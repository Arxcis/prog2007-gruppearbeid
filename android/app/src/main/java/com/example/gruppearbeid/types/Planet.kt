package com.example.gruppearbeid.types

import java.io.Serializable

data class Planet(
    /** "name": "Tatooine" */
    val name: String,

    /** "residents": ["https://swapi.dev/api/people/1/", ...] */
    val residents: ArrayList<String>,
) : Serializable
