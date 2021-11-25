package com.example.gruppearbeid.types

import java.io.Serializable

data class Person(
    /** "name": "Luke Skywalker" */
    val name: String,

    /** "films": ["https://swapi.dev/api/films/1/", ...] */
    val films: ArrayList<String>,
) : Serializable
