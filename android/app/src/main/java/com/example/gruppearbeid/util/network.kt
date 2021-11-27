package com.example.gruppearbeid.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.adapters.StarshipsAdapter
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

interface INetwork {
    var bitmap: Bitmap

    fun getFilms(search: String, onSuccess: (films: ArrayList<Film>) -> Unit, onError: (text: String) -> Unit)
    fun getPeople(search: String, onSuccess: (people: ArrayList<Person>) -> Unit, onError: (text: String) -> Unit)
    fun getPlanets(search: String, onSuccess: (planets: ArrayList<Planet>) -> Unit, onError: (text: String) -> Unit)
    fun getStarships(search: String, onSuccess: (starships: ArrayList<Starship>) -> Unit, onError: (text: String) -> Unit)
    fun getSpeciesList(search: String, onSuccess: (speciesList: ArrayList<Species>) -> Unit, onError: (text: String) -> Unit)
    fun getFilmsByURL(urls: ArrayList<String>, films: ArrayList<Film>, adapter: FilmsAdapter, onError: (text: String) -> Unit)
    fun getPeopleByURL(urls: ArrayList<String>, people: ArrayList<Person>, adapter: PeopleAdapter, onError: (text: String) -> Unit)
    fun getStarshipsByURL(urls: ArrayList<String>, starships: ArrayList<Starship>, adapter: StarshipsAdapter, onError: (text: String) -> Unit)
    fun getPlanetsByURL(urls: ArrayList<String>, planets: ArrayList<Planet>, adapter: PlanetsAdapter, onError: (text: String) -> Unit)
    fun getSpeciesByURL(urls: ArrayList<String>, speciesList: ArrayList<Species>, adapter: SpeciesListAdapter, onError: (text: String) -> Unit)
    fun downloadImage(url: String, activity: Activity, updateImage: () -> Unit,permission: () -> Boolean, appContext: Context)
}

class Network(private val ctx: Context) : INetwork {
    private val etagsCache: ICache = SimpleCache(ctx, Constants.CACHE_ETAGS)
    private val responseCache: ICache = SimpleCache(ctx, Constants.CACHE_REQUESTS)
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private val TAG = "Network.util"
    private val BASE_URL = "https://swapi.dev/api"

    override lateinit var bitmap: Bitmap

    override fun downloadImage(url: String, activity: Activity, updateImage: () -> Unit,permission: () -> Boolean, appContext: Context)
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
                        Storage.saveImage(bitmap, permission, appContext)
                        //image.setImageBitmap(bitmap)
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

    override fun getFilms(search: String, onSuccess: (films: ArrayList<Film>) -> Unit, onError: (text: String) -> Unit) {
        val href = "$BASE_URL/films?search=${search}"

        executor.execute{
            // 0. Cache first
            val etag = etagsCache.getValue(href, null)
            val cachedResponse = responseCache.getValue(etag, null)
            cachedResponse?.apply {
                val films = parseFilms(cachedResponse)
                handler.post {
                    onSuccess(films)
                }
            }

            // 1. Do HTTP Request
            var res: String?
            try {
                res = readTextFromUrl(href)
            } catch (err: IOException) {
                Log.w("network.getFilms", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }
            val films = parseFilms(res)
            handler.post {
                onSuccess(films)
            }
        }
    }

    override fun getPeople(search: String, onSuccess: (people: ArrayList<Person>) -> Unit, onError: (text: String) -> Unit) {
        val href = "$BASE_URL/people?search=${search}"

        executor.execute{
            // 0. Cache first
            val etag = etagsCache.getValue(href, null)
            val cachedResponse = responseCache.getValue(etag, null)
            cachedResponse?.apply {
                val people = parsePeople(cachedResponse)
                handler.post {
                    onSuccess(people)
                }
            }

            // 1. Do HTTP Request
            var res: String?
            try {
                res = readTextFromUrl(href)
            } catch (err: IOException) {
                Log.w("network.getPeople", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }
            val people = parsePeople(res)
            handler.post {
                onSuccess(people)
            }
        }
    }

    override fun getPlanets(search: String, onSuccess: (planets: ArrayList<Planet>) -> Unit, onError: (text: String) -> Unit) {
        val href = "$BASE_URL/planets?search=${search}"

        executor.execute{
            // 0. Cache first
            val etag = etagsCache.getValue(href, null)
            val cachedResponse = responseCache.getValue(etag, null)
            cachedResponse?.apply {
                val planets = parsePlanets(cachedResponse)
                handler.post {
                    onSuccess(planets)
                }
            }

            // 1. Do HTTP Request
            var res: String?
            try {
                res = readTextFromUrl(href)
            } catch (err: IOException) {
                Log.w("network.getPlanets", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }
            val planets = parsePlanets(res)
            handler.post {
                onSuccess(planets)
            }
        }
    }

    override fun getStarships(search: String, onSuccess: (starships: ArrayList<Starship>) -> Unit, onError: (text: String) -> Unit) {
        val href = "$BASE_URL/starships?search=${search}"

        executor.execute{
            // 0. Cache first
            val etag = etagsCache.getValue(href, null)
            val cachedResponse = responseCache.getValue(etag, null)
            cachedResponse?.apply {
                val starships = parseStarships(cachedResponse)
                handler.post {
                    onSuccess(starships)
                }
            }

            // 1. Do HTTP Request
            var res: String?
            try {
                res = readTextFromUrl(href)
            } catch (err: IOException) {
                Log.w("network.getStarships", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }
            val starships = parseStarships(res)
            handler.post {
                onSuccess(starships)
            }
        }
    }

    override fun getSpeciesList(search: String, onSuccess: (speciesList: ArrayList<Species>) -> Unit, onError: (text: String) -> Unit) {
        val href = "$BASE_URL/species?search=${search}"

        executor.execute{
            // 0. Cache first
            val etag = etagsCache.getValue(href, null)
            val cachedResponse = responseCache.getValue(etag, null)
            cachedResponse?.apply {
                val list = parseSpeciesList(cachedResponse)
                handler.post {
                    onSuccess(list)
                }
            }

            // 1. Do HTTP Request
            var res: String?
            try {
                res = readTextFromUrl(href)
            } catch (err: IOException) {
                Log.w("network.getSpeciesList", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }
            val list = parseSpeciesList(res)
            handler.post {
                onSuccess(list)
            }
        }
    }

    override fun getFilmsByURL(urls: ArrayList<String>, films: ArrayList<Film>, adapter: FilmsAdapter, onError: (text: String) -> Unit) {
        executor.execute{
            for (url in urls) {
                var json: JSONObject? = null
                try {
                    json = readJsonFromUrl(url)
                } catch (err: IOException) {
                    Log.w("network.getFilmsBy", "No connection...", err)
                    handler.post { onError("No connection...") }
                    return@execute
                }
                val film = parseFilm(json)
                films.add(film)
            }

            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun getPeopleByURL(urls: ArrayList<String>, people: ArrayList<Person>, adapter: PeopleAdapter, onError: (text: String) -> Unit) {
        executor.execute{
            for (url in urls) {
                var json: JSONObject? = null
                try {
                    json = readJsonFromUrl(url)
                } catch (err: IOException) {
                    Log.w("network.getPeopleBy", "No connection...", err)
                    handler.post { onError("No connection...") }
                    return@execute
                }
                val person = parsePerson(json)
                people.add(person)
            }

            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun getStarshipsByURL(urls: ArrayList<String>, starships: ArrayList<Starship>, adapter: StarshipsAdapter, onError: (text: String) -> Unit) {
        executor.execute{
            for (url in urls) {
                var json: JSONObject? = null
                try {
                    json = readJsonFromUrl(url)
                } catch (err: IOException) {
                    Log.w("network.getStarshipsBy", "No connection...", err)
                    handler.post { onError("No connection...") }
                    return@execute
                }

                val starship = parseStarship(json)
                starships.add(starship)
            }

            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun getPlanetsByURL(urls: ArrayList<String>, planets: ArrayList<Planet>, adapter: PlanetsAdapter, onError: (text: String) -> Unit) {
        executor.execute{
            for (url in urls) {
                var json: JSONObject? = null
                try {
                    json = readJsonFromUrl(url)
                } catch (err: IOException) {
                    Log.w("network.getPlanetsBy", "No connection...", err)
                    handler.post { onError("No connection...") }
                    return@execute
                }

                val planet = parsePlanet(json)
                planets.add(planet)
            }

            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun getSpeciesByURL(urls: ArrayList<String>, speciesList: ArrayList<Species>, adapter: SpeciesListAdapter, onError: (text: String) -> Unit) {
        executor.execute{
            for (url in urls) {
                var json: JSONObject? = null
                try {
                    json = readJsonFromUrl(url)
                } catch (err: IOException) {
                    Log.w("network.getSpeciesBy", "No connection...", err)
                    handler.post { onError("No connection...") }
                    return@execute
                }

                val species = parseSpecies(json)
                speciesList.add(species)
            }

            handler.post {
                adapter.notifyDataSetChanged()
            }
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


private fun parseStarships(text: String): ArrayList<Starship> {
    val json = JSONObject(text)
    val results = json.getJSONArray("results")

    val list = ArrayList<Starship>()
    for (i in 0 until results.length()) {
        val obj = results.getJSONObject(i)
        val item = parseStarship(obj)
        list.add(item)
    }
    return list
}


private fun parsePlanets(text: String): ArrayList<Planet> {
    val json = JSONObject(text)
    val results = json.getJSONArray("results")

    val list = ArrayList<Planet>()
    for (i in 0 until results.length()) {
        val obj = results.getJSONObject(i)
        val item = parsePlanet(obj)
        list.add(item)
    }
    return list
}


private fun parsePeople(text: String): ArrayList<Person> {
    val json = JSONObject(text)
    val results = json.getJSONArray("results")

    val list = ArrayList<Person>()
    for (i in 0 until results.length()) {
        val obj = results.getJSONObject(i)
        val item = parsePerson(obj)
        list.add(item)
    }
    return list
}


private fun parseFilms(text: String): ArrayList<Film> {
    val json = JSONObject(text)
    val results = json.getJSONArray("results")

    val films = ArrayList<Film>()
    for (i in 0 until results.length()) {
        val item = results.getJSONObject(i)
        val film = parseFilm(item)
        films.add(film)
    }
    return films
}


private fun parseSpeciesList(text: String): ArrayList<Species> {
    val json = JSONObject(text)
    val results = json.getJSONArray("results")

    val list = ArrayList<Species>()
    for (i in 0 until results.length()) {
        val obj = results.getJSONObject(i)
        val item = parseSpecies(obj)
        list.add(item)
    }
    return list
}


private fun parseStarship(item: JSONObject): Starship {
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
