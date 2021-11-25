package com.example.gruppearbeid.types

import java.io.Serializable

data class Person(
    /** "name": "Luke Skywalker" */
    val name: String,

    val height: String,

    val birth_year: String,

    val gender: String,

    val homeworld: String
) : Serializable
