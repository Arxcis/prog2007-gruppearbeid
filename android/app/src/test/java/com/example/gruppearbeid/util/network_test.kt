package com.example.gruppearbeid.util

import org.junit.Test

import org.junit.Assert.*

class ParseResultsTest {
    @Test
    fun testNext() {
        val res = parseResults("""
           { 
                "count": 82,
                "next": "https://swapi.dev/api/people/?page=2",
                "previous": null,
                "results": [],
            }
        """.trimIndent(), ::parsePerson)

        assertEquals(res.results.size, 0)
        assertEquals(res.next, "https://swapi.dev/api/people/?page=2")
        assertNull(res.prev)
        assertEquals(res.pageCount, 9)
        assertEquals(res.page, 1)
        assertEquals(res.count, 82)
    }

    @Test
    fun testPrev() {
        val res = parseResults("""
           { 
                "count": 82,
                "next": null,
                "previous": "https://swapi.dev/api/people/?page=8",
                "results": [],
            }
        """.trimIndent(), ::parsePerson)

        assertEquals(res.results.size, 0)
        assertNull(res.next)
        assertEquals(res.prev, "https://swapi.dev/api/people/?page=8")
        assertEquals(res.pageCount, 9)
        assertEquals(res.page, 9)
        assertEquals(res.count, 82)
    }

    @Test
    fun testPrevAndNext() {
        val res = parseResults("""
           { 
                "count": 82,
                "next": "https://swapi.dev/api/people/?page=3",
                "previous": "https://swapi.dev/api/people/?page=1",
                "results": [],
            }
        """.trimIndent(), ::parsePerson)

        assertEquals(res.results.size, 0)
        assertEquals(res.next, "https://swapi.dev/api/people/?page=3")
        assertEquals(res.prev, "https://swapi.dev/api/people/?page=1")
        assertEquals(res.pageCount, 9)
        assertEquals(res.page, 2)
        assertEquals(res.count, 82)
    }

    @Test
    fun testPrevNextBothNull() {
        val res = parseResults("""
           { 
                "count": 2,
                "next": null,
                "previous": null,
                "results": [],
            }
        """.trimIndent(), ::parsePerson)

        assertEquals(res.results.size, 0)
        assertNull(res.next)
        assertNull(res.prev)
        assertEquals(res.pageCount, 1)
        assertEquals(res.page, 1)
        assertEquals(res.count, 2)
    }

    @Test
    fun testCountIsZero() {
        val res = parseResults("""
           { 
                "count": 0,
                "next": null,
                "previous": null,
                "results": [],
            }
        """.trimIndent(), ::parsePerson)

        assertEquals(res.results.size, 0)
        assertNull(res.next)
        assertNull(res.prev)
        assertEquals(1, res.pageCount)
        assertEquals(res.page, 1)
        assertEquals(res.count, 0)
    }

    @Test
    fun testCountIsARoundNumber() {
        val res = parseResults("""
           { 
                "count": 60,
                "next": "https://swapi.dev/api/planets/?page=2",
                "previous": null,
                "results": [],
            }
        """.trimIndent(), ::parsePerson)

        assertEquals(0, res.results.size)
        assertEquals("https://swapi.dev/api/planets/?page=2", res.next)
        assertNull(res.prev)
        assertEquals(6, res.pageCount, )
        assertEquals(1, res.page)
        assertEquals(60, res.count)
    }
}