package com.example.gruppearbeid.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import androidx.annotation.RequiresApi
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.types.Starship
import com.example.gruppearbeid.adapters.*
import com.example.gruppearbeid.types.*
import java.util.concurrent.Executors
import org.json.JSONObject

import org.json.JSONException
import java.io.*
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import java.net.HttpURLConnection
import kotlin.math.ceil
import kotlin.math.max

interface INetwork {
    var bitmap: Bitmap

    fun downloadImage(url: String, activity: Activity, updateImage: () -> Unit, fileName: String, permission: () -> Boolean, appContext: Context)
    fun searchFilms(search: String,       onSuccess: (res: Results<Film>) -> Unit,     onError: (text: String) -> Unit)
    fun searchPeople(search: String,      onSuccess: (res: Results<Person>) -> Unit,   onError: (text: String) -> Unit)
    fun searchPlanets(search: String,     onSuccess: (res: Results<Planet>) -> Unit,   onError: (text: String) -> Unit)
    fun searchStarships(search: String,   onSuccess: (res: Results<Starship>) -> Unit, onError: (text: String) -> Unit)
    fun searchSpeciesList(search: String, onSuccess: (res: Results<Species>) -> Unit,  onError: (text: String) -> Unit)

    fun getFilms(url: String,       onSuccess: (res: Results<Film>) -> Unit,     onError: (text: String) -> Unit)
    fun getPeople(url: String,      onSuccess: (res: Results<Person>) -> Unit,   onError: (text: String) -> Unit)
    fun getPlanets(url: String,     onSuccess: (res: Results<Planet>) -> Unit,   onError: (text: String) -> Unit)
    fun getStarships(url: String,   onSuccess: (res: Results<Starship>) -> Unit, onError: (text: String) -> Unit)
    fun getSpeciesList(url: String, onSuccess: (res: Results<Species>) -> Unit,  onError: (text: String) -> Unit)

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

    private val TAG = "Network.util"
    private val BASE_URL = "https://swapi.dev/api"

    override lateinit var bitmap: Bitmap

    @RequiresApi(Build.VERSION_CODES.P)
    override fun downloadImage(url: String, activity: Activity, updateImage: () -> Unit, fileName: String, permission: () -> Boolean, appContext: Context)
    //trying this:
    //https://stackoverflow.com/questions/18210700/best-method-to-download-image-from-url-in-android
    {
        executor.execute {
            if (Patterns.WEB_URL.matcher(url).matches())
            {
                try {
                    val realURL = URL(url)
                    val connection = realURL.openConnection() as HttpsURLConnection

                    val bitmapOption = BitmapFactory.Options().apply {
                        inSampleSize = 2    //load image where widht and height is divided by two to save memory
                                            //when displaying picture
                    }

                    val input = connection.inputStream
                    bitmap = BitmapFactory.decodeStream(input,null,bitmapOption)!!

                    if (bitmap != null) {
                                                            //Got the message "only the original view hierarchy
                                                           //can touch its views. Therefore the imageView is updated
                                                               //on its UI thread instead of through the thread from
                                                                   //Network.util.
                        activity.runOnUiThread(object : Runnable {
                            override fun run() {
                                updateImage()
                            }
                        })
                        Storage.saveImage(bitmap, fileName, permission, activity, updateImage)
                    } else {
                        Log.d(TAG, "bitmap is null")
                    }

                } catch(ex: SocketTimeoutException) {
                    Log.d(TAG, "socket timed out")
                } catch(ex: IOException) {
                    Log.d(TAG, "Input output error. Is WIFI enabled?")
                    Log.d(TAG, ex.message.toString())
                } catch (ex: IllegalArgumentException)
                {
                    Log.d("Planets", "illegalARgumentException in BitmapFactory.decodeStream()")
                }
                catch(ex: Exception) {
                    Log.d(TAG, "an exception occurred")
                    Log.d(TAG, "${ex.message}")
                }

            }else {
                Log.d(TAG, "the format of the URL was incorrect.")
            }

        }
    }

    override fun searchFilms(search: String, onSuccess: (res: Results<Film>) -> Unit, onError: (text: String) -> Unit) {
        getThings("$BASE_URL/films?search=${search}", ::parseFilms, onSuccess, onError)
    }

    override fun searchPeople(search: String, onSuccess: (res: Results<Person>) -> Unit, onError: (text: String) -> Unit) {
        getThings("$BASE_URL/people?search=${search}", ::parsePeople, onSuccess, onError)
    }

    override fun searchPlanets(search: String, onSuccess: (res: Results<Planet>) -> Unit, onError: (text: String) -> Unit) {
        getThings("$BASE_URL/planets?search=${search}", ::parsePlanets, onSuccess, onError)
    }

    override fun searchStarships(search: String, onSuccess: (res: Results<Starship>) -> Unit, onError: (text: String) -> Unit) {
        getThings("$BASE_URL/starships?search=${search}", ::parseStarships, onSuccess, onError)
    }

    override fun searchSpeciesList(search: String, onSuccess: (res: Results<Species>) -> Unit, onError: (text: String) -> Unit) {
        getThings("$BASE_URL/species?search=${search}", ::parseSpeciesList, onSuccess, onError)
    }

    override fun getFilms(url: String, onSuccess: (res: Results<Film>) -> Unit, onError: (text: String) -> Unit) {
        getThings(url, ::parseFilms, onSuccess, onError)
    }

    override fun getPeople(url: String, onSuccess: (res: Results<Person>) -> Unit, onError: (text: String) -> Unit) {
        getThings(url, ::parsePeople, onSuccess, onError)
    }

    override fun getPlanets(url: String, onSuccess: (res: Results<Planet>) -> Unit, onError: (text: String) -> Unit) {
        getThings(url, ::parsePlanets, onSuccess, onError)
    }

    override fun getStarships(url: String, onSuccess: (res: Results<Starship>) -> Unit, onError: (text: String) -> Unit) {
        getThings(url, ::parseStarships, onSuccess, onError)
    }

    override fun getSpeciesList(url: String, onSuccess: (res: Results<Species>) -> Unit, onError: (text: String) -> Unit) {
        getThings(url, ::parseSpeciesList, onSuccess, onError)
    }

    private fun <Thing>getThings(
        url: String,
        parse: (text: String) -> Results<Thing>,
        onSuccess: (results: Results<Thing>) -> Unit,
        onError: (text: String) -> Unit,
    ) {
        executor.execute{
            // 1. Cache first
            val cachedEtag = etagsCache.getValue(url, null)
            val cachedResponse = responseCache.getValue(cachedEtag, null)
            cachedResponse?.run {
                val things = parse(cachedResponse)
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
            val things = parse(text)
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

private fun parseStarships(res: String) = parseResults(res, ::parseStarship)
private fun parsePlanets(res: String) = parseResults(res, ::parsePlanet)
private fun parsePeople(res: String) = parseResults(res, ::parsePerson)
private fun parseFilms(res: String) = parseResults(res, ::parseFilm)
private fun parseSpeciesList(res: String) = parseResults(res, ::parseSpecies)

fun <Thing>parseResults(text: String, parseThing: (text: JSONObject) -> Thing): Results<Thing> {
    val json = JSONObject(text)
    val prev: String? =  if (!json.isNull("previous")) { json.getString("previous") } else { null }
    val next: String? = if (!json.isNull("next")) { json.getString("next") } else { null }
    val results = json.getJSONArray("results")
    val count = json.getInt("count")

    val things = ArrayList<Thing>()

    for (i in 0 until results.length()) {
        val obj = results.getJSONObject(i)
        val item = parseThing(obj)
        things.add(item)
    }

    val page = when {
        next != null -> next[next.length - 1].digitToInt() - 1
        prev != null -> prev[prev.length - 1].digitToInt() + 1
        else -> 1
    }

    val pageCount = max(ceil(count.toDouble() / Constants.RESULTS_PAGE_SIZE).toInt(), 1)

    return Results(
        things,
        count = count,
        pageCount = pageCount,
        page = page,
        prev = prev,
        next = next
    )
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
        url = item.getString("url"),
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
