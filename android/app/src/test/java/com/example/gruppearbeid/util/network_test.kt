package com.example.gruppearbeid.util

import org.junit.Test

import org.junit.Assert.*

class ParseResultsTest {
    @Test
    fun testNext() {
        val (people, prev, next) = parseResults("""
           { 
                "count": 82,
                "next": "https://swapi.dev/api/people/?page=2",
                "previous": null,
                "results": [],
            }
        """.trimIndent(), ::parsePerson)

        assertEquals(people.size, 0)
        assertEquals(next, "https://swapi.dev/api/people/?page=2")
        assertNull(prev)
    }

    @Test
    fun testPrev() {
        val (people, prev, next) = parseResults("""
           { 
                "count": 82,
                "next": null,
                "previous": "https://swapi.dev/api/people/?page=9",
                "results": [],
            }
        """.trimIndent(), ::parsePerson)

        assertEquals(people.size, 0)
        assertNull(next)
        assertEquals(prev, "https://swapi.dev/api/people/?page=9")
    }

    @Test
    fun testPrevAndNext() {
        val (people, prev, next) = parseResults("""
           { 
                "count": 82,
                "next": "https://swapi.dev/api/people/?page=3",
                "previous": "https://swapi.dev/api/people/?page=1",
                "results": [],
            }
        """.trimIndent(), ::parsePerson)

        assertEquals(people.size, 0)
        assertEquals(next, "https://swapi.dev/api/people/?page=3")
        assertNotNull(prev, "https://swapi.dev/api/people/?page=1")
    }
}