package com.example.gruppearbeid.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.gruppearbeid.adapters.*
import com.example.gruppearbeid.types.*
import com.example.gruppearbeid.util.Constants.Companion.BASE_URL
import java.util.concurrent.Executors
import org.json.JSONObject

import org.json.JSONException
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

interface INetwork {
    fun getFilms(search: String,       onSuccess: (films: List<Film>) -> Unit,          onError: (text: String) -> Unit)
    fun getPeople(search: String,      onSuccess: (people: List<Person>) -> Unit,       onError: (text: String) -> Unit)
    fun getPlanets(search: String,     onSuccess: (planets: List<Planet>) -> Unit,      onError: (text: String) -> Unit)
    fun getStarships(search: String,   onSuccess: (starships: List<Starship>) -> Unit,  onError: (text: String) -> Unit)
    fun getSpeciesList(search: String, onSuccess: (speciesList: List<Species>) -> Unit, onError: (text: String) -> Unit)

    fun getFilmsByURL(urls: List<String>,     onSuccess: (films: List<Film>) -> Unit,          onError: (text: String) -> Unit)
    fun getPeopleByURL(urls: List<String>,    onSuccess: (people: List<Person>) -> Unit,       onError: (text: String) -> Unit)
    fun getPlanetsByURL(urls: List<String>,   onSuccess: (planets: List<Planet>) -> Unit,      onError: (text: String) -> Unit)
    fun getStarshipsByURL(urls: List<String>, onSuccess: (starships: List<Starship>) -> Unit,  onError: (text: String) -> Unit)
    fun getSpeciesByURL(urls: List<String>,   onSuccess: (speciesList: List<Species>) -> Unit, onError: (text: String) -> Unit)
}

class Network(private val ctx: Context) : INetwork {
    private val etagsCache: ICache = SimpleCache(ctx, Constants.CACHE_ETAGS)
    private val responseCache: ICache = SimpleCache(ctx, Constants.CACHE_REQUESTS)
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    override fun getFilms(search: String, onSuccess: (films: List<Film>) -> Unit, onError: (text: String) -> Unit) {
        getThings("$BASE_URL/films?search=${search}", ::parseFilms, onSuccess, onError)
    }

    override fun getPeople(search: String, onSuccess: (people: List<Person>) -> Unit, onError: (text: String) -> Unit) {
        getThings("$BASE_URL/people?search=${search}", ::parsePeople, onSuccess, onError)
    }

    override fun getPlanets(search: String, onSuccess: (planets: List<Planet>) -> Unit, onError: (text: String) -> Unit) {
        getThings("$BASE_URL/planets?search=${search}", ::parsePlanets, onSuccess, onError)
    }

    override fun getStarships(search: String, onSuccess: (starships: List<Starship>) -> Unit, onError: (text: String) -> Unit) {
        getThings("$BASE_URL/starships?search=${search}", ::parseStarships, onSuccess, onError)
    }

    override fun getSpeciesList(search: String, onSuccess: (speciesList: List<Species>) -> Unit, onError: (text: String) -> Unit) {
        getThings("$BASE_URL/species?search=${search}", ::parseSpeciesList, onSuccess, onError)
    }

    private fun <Thing>getThings(
        url: String,
        parse: (text: String) -> ParsedThings<Thing>,
        onSuccess: (speciesList: List<Thing>) -> Unit,
        onError: (text: String) -> Unit,
    ) {
        executor.execute{
            // 1. Cache first
            val cachedEtag = etagsCache.getValue(url, null)
            val cachedResponse = responseCache.getValue(cachedEtag, null)
            cachedResponse?.run {
                val (things) = parse(cachedResponse)
                handler.post{ onSuccess(things) }
            }

            // 2. Do HTTP Request
            var text: String = try {
                readTextFromUrl(url)
            } catch (err: IOException) {
                Log.w("network.getThings", "Tried to GET $url: No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }
            val (things) = parse(text)
            handler.post {
                onSuccess(things)
            }
        }
    }

    override fun getFilmsByURL(urls: List<String>, onSuccess: (films: List<Film>) -> Unit, onError: (text: String) -> Unit) {
        getThingsByURL(urls, ::parseFilm, onSuccess, onError)
    }

    override fun getPeopleByURL(urls: List<String>, onSuccess: (people: List<Person>) -> Unit, onError: (text: String) -> Unit) {
        getThingsByURL(urls, ::parsePerson, onSuccess, onError)
    }

    override fun getStarshipsByURL(urls: List<String>, onSuccess: (starships: List<Starship>) -> Unit, onError: (text: String) -> Unit) {
        getThingsByURL(urls, ::parseStarship, onSuccess, onError)
    }

    override fun getPlanetsByURL(urls: List<String>, onSuccess: (planets: List<Planet>) -> Unit, onError: (text: String) -> Unit) {
        getThingsByURL(urls, ::parsePlanet, onSuccess, onError)
    }

    override fun getSpeciesByURL(urls: List<String>, onSuccess: (speciesList: List<Species>) -> Unit, onError: (text: String) -> Unit) {
        getThingsByURL(urls, ::parseSpecies, onSuccess, onError)
    }

    private fun <Thing>getThingsByURL(
        urls: List<String>,
        parse: (json: JSONObject) -> Thing,
        onSuccess: (films: List<Thing>) -> Unit,
        onError: (text: String) -> Unit,
    ) {
        executor.execute{
            // 1. Cache first
            var cachedThings = urls.mapNotNull{ url ->
                val cachedEtag = etagsCache.getValue(url, null)
                val cachedResponse = responseCache.getValue(cachedEtag, null)
                cachedResponse?.run {
                    val json = JSONObject(cachedResponse)
                    parse(json)
                }
            }
            handler.post {  onSuccess(cachedThings) }

            // 2. Do HTTP requests
            val fetchedThings = urls.mapNotNull{ url ->
                try {
                    val json = readJsonFromUrl(url)
                    parse(json)
                } catch (err: IOException) {
                    Log.w("getThingsByURL", "Tried to GET ${url}: No connection...", err)
                    handler.post { onError("No connection...") }
                    null
                }
            }
            handler.post { onSuccess(fetchedThings) }
        }
    }


    /** See @url https://stackoverflow.com/a/4308662 */
    @Throws(IOException::class, JSONException::class)
    private fun readJsonFromUrl(href: String): JSONObject {
        return JSONObject(readTextFromUrl(href))
    }
    /** See @url https://stackoverflow.com/a/4308662 */
    @Throws(IOException::class)
    private fun readTextFromUrl(href: String): String {
        val cachedEtag = etagsCache.getValue(href, null)
        val cachedResponse = responseCache.getValue(cachedEtag, null)

        var url = URL(href)
        var connection = url.openConnection() as HttpURLConnection
        cachedEtag?.apply{ connection.setRequestProperty("If-None-Match", cachedEtag) }

        connection.connect() // Do network-request

        var status = connection.responseCode
        if (status == 304 && cachedResponse != null) {
            Log.d("readJsonFromUrl", "Cache hit! Href: $href, ETag: $cachedEtag")
            return cachedResponse
        } else if (status == 304 && cachedResponse == null) {
            Log.w("readJsonFromUrl", "WARN Got 304, but no cached response was found locally. Forcing re-fetch from network...")
            url = URL(href)
            connection = url.openConnection() as HttpURLConnection
            connection.connect() // Retry network-request without "If-None-Match"-header
        } else {
            Log.d("readJsonFromUrl", "Cache miss! Href: $href, ETag: $cachedEtag")
        }

        val etag = connection.headerFields["ETag"]?.get(0)
        val text = connection.inputStream.bufferedReader().readText()

        etagsCache.setValue(href, etag)
        responseCache.setValue(etag, text)

        return text
    }
}


private fun parseStarships(text: String) = parseThings(text, ::parseStarship)
private fun parsePlanets(text: String) = parseThings(text, ::parsePlanet)
private fun parsePeople(text: String) = parseThings(text, ::parsePerson)
private fun parseFilms(text: String) = parseThings(text, ::parseFilm)
private fun parseSpeciesList(text: String) = parseThings(text, ::parseSpecies)

data class ParsedThings<Thing>(val things: ArrayList<Thing>, val prev: String?, val next: String?)

fun <Thing>parseThings(text: String, parseThing: (text: JSONObject) -> Thing): ParsedThings<Thing> {
    val json = JSONObject(text)
    val prev: String? =  if (!json.isNull("previous")) { json.getString("previous") } else { null }
    val next: String? = if (!json.isNull("next")) { json.getString("next") } else { null }
    val results = json.getJSONArray("results")

    val things = ArrayList<Thing>()

    for (i in 0 until results.length()) {
        val obj = results.getJSONObject(i)
        val item = parseThing(obj)
        things.add(item)
    }
    return ParsedThings(things, prev, next)
}


fun parseStarship(item: JSONObject): Starship {
    // films:
    val films = ArrayList<String>()
    val jsonFilms = item.getJSONArray("films")
    for (k in 0 until jsonFilms.length()) {
        films.add(jsonFilms.get(k).toString())
    }

    // pilots:
    val pilots = ArrayList<String>()
    val jsonPilots = item.getJSONArray("pilots")
    for (k in 0 until jsonPilots.length()) {
        pilots.add(jsonPilots.get(k).toString())
    }

    return Starship(
        name = item.getString("name"),
        model = item.getString("model"),
        manufacturer = item.getString("manufacturer"),
        length = item.getString("length"),
        max_atmosphering_speed = item.getString("max_atmosphering_speed"),
        crew = item.getString("crew"),
        passengers = item.getString("passengers"),
        starship_class = item.getString("starship_class"),
        films = films,
        pilots = pilots,
    )
}


fun parsePlanet(item: JSONObject): Planet {
    // Residents:
    val residents = ArrayList<String>()
    val jsonResidents = item.getJSONArray("residents")
    for (k in 0 until jsonResidents.length()) {
        residents.add(jsonResidents.get(k).toString())
    }
    // Films:
    val films = ArrayList<String>()
    val jsonFilms = item.getJSONArray("films")
    for (k in 0 until jsonFilms.length()) {
        films.add(jsonFilms.get(k).toString())
    }

    return Planet(
        name = item.getString("name"),
        rotation_period = item.getString("rotation_period"),
        orbital_period = item.getString("orbital_period"),
        climate = item.getString("climate"),
        terrain = item.getString("terrain"),
        population = item.getString("population"),
        residents = residents,
        films = films,
    )
}


fun parsePerson(item: JSONObject): Person {
    // Homeworld:
    val homeworld = arrayListOf(item.getString("homeworld"))

    // Films:
    val films = ArrayList<String>()
    val jsonFilms = item.getJSONArray("films")
    for (k in 0 until jsonFilms.length()) {
        films.add(jsonFilms.get(k).toString())
    }
    // Starships:
    val starships = ArrayList<String>()
    val jsonStarships = item.getJSONArray("starships")
    for (k in 0 until jsonStarships.length()) {
        starships.add(jsonStarships.get(k).toString())
    }
    // species:
    val species = ArrayList<String>()
    val jsonSpecies = item.getJSONArray("species")
    for (k in 0 until jsonSpecies.length()) {
        species.add(jsonSpecies.get(k).toString())
    }

    return Person(
        name = item.getString("name"),
        height = item.getString("height"),
        birth_year = item.getString("birth_year"),
        gender = item.getString("gender"),
        homeworld = homeworld,
        films = films,
        starships = starships,
        species = species,
    )
}


fun parseFilm(item: JSONObject): Film {
    // Characters:
    val characters = ArrayList<String>()
    val jsonCharacters = item.getJSONArray("characters")
    for (k in 0 until jsonCharacters.length()) {
        characters.add(jsonCharacters.get(k).toString())
    }
    // Planets:
    val planets = ArrayList<String>()
    val jsonPlanets = item.getJSONArray("planets")
    for (k in 0 until jsonPlanets.length()) {
        planets.add(jsonPlanets.get(k).toString())
    }
    // starships:
    val starships = ArrayList<String>()
    val jsonStarships = item.getJSONArray("starships")
    for (k in 0 until jsonStarships.length()) {
        starships.add(jsonStarships.get(k).toString())
    }
    // species:
    val species = ArrayList<String>()
    val jsonSpecies = item.getJSONArray("species")
    for (k in 0 until jsonSpecies.length()) {
        species.add(jsonSpecies.get(k).toString())
    }

    return Film(
        title = item.getString("title"),
        episode_id = item.getInt("episode_id"),
        director = item.getString("director"),
        producer = item.getString("producer"),
        release_date = item.getString("release_date"),
        characters = characters,
        planets = planets,
        starships = starships,
        species = species,
    )
}


fun parseSpecies(item: JSONObject): Species {
    // Planets:
    val people = ArrayList<String>()
    val jsonPeople = item.getJSONArray("people")
    for (k in 0 until jsonPeople.length()) {
        people.add(jsonPeople.get(k).toString())
    }
    // films:
    val films = ArrayList<String>()
    val jsonFilms = item.getJSONArray("films")
    for (k in 0 until jsonFilms.length()) {
        films.add(jsonFilms.get(k).toString())
    }

    return Species(
        name = item.getString("name"),
        people = people,
        films = films,
    )
}
