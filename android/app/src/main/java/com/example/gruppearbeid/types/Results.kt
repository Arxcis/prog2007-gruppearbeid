package com.example.gruppearbeid.types

data class Results<Thing>(
    val results: ArrayList<Thing>, 
    val prev: String?, 
    val next: String?
)
