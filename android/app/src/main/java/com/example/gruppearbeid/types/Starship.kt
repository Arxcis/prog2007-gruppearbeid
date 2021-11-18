package com.example.gruppearbeid.types

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
    )