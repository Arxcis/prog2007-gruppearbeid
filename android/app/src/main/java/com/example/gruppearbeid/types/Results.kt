package com.example.gruppearbeid.types


data class Results<Thing>(
    val results: ArrayList<Thing>,
    val pageCount: Int,
    val page: Int,
    val count: Int,
    val prev: String?,
    val next: String?,
)
