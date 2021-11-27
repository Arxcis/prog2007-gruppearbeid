package com.example.gruppearbeid.types

interface Pagination {
    val pageCount: Int
    val page: Int
    val count: Int
    val prev: String?
    val next: String?
}

data class Results<Thing>(
    val results: ArrayList<Thing>,
    override val pageCount: Int,
    override val page: Int,
    override val count: Int,
    override val prev: String?,
    override val next: String?,
): Pagination
