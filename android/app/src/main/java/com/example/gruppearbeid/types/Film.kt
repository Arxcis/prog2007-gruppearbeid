package com.example.gruppearbeid.types

/** Typedef: https://swapi.dev/documentation#films */
data class Film(
    /** Example: "George Lucas" */
    val director: String,

    /** Example: 4 */
    val episode_id: Int,

    /** Example: "It is a period of civil war.\n\nRebel spaceships,...." */
    val opening_crawl: Int,

    /** Example: "Gary Kurtz, Rick McCallum" */
    val producer: String,

    /** Example: "1977-05-25" */
    val release_date: String,

    /** Example: "A New Hope" */
    val title: String,

    /** Example: "https://swapi.dev/api/films/1/" */
    val url: String,

    /** Example: ["https://swapi.dev/api/people/1/", ...] */
    val characters: ArrayList<String>,

    /** Example: ["https://swapi.dev/api/planets/1/", ...] */
    val planets: ArrayList<String>,

    /** Example: ["https://swapi.dev/api/species/1/", ...] */
    val species: ArrayList<String>,

    /** Example: ["https://swapi.dev/api/starships/1/", ...] */
    val starships: ArrayList<String>,

    /** Example: ["https://swapi.dev/api/vehicles/1/", ...] */
    val vehicles: ArrayList<String>
);