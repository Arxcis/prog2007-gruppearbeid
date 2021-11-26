package com.example.gruppearbeid.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
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
import java.lang.StringBuilder
import java.net.SocketTimeoutException
import java.net.URL
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection

interface INetwork {
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
}

object Network : INetwork {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private val TAG = "Network.util"
    private const val BASE_URL = "https://swapi.dev/api"

    lateinit var bitmap: Bitmap

    fun downloadImage(url: String, activity: Activity, updateImage: () -> Unit)
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
                        //image.setImageBitmap(bitmap)
                    } else {
                        Log.d(TAG, "bitmap is null")
                    }

                } catch(ex: SocketTimeoutException) {
                    Log.d(TAG, "socket timed out")
                } catch(ex: IOException) {
                    Log.d(TAG, "Input output error. Is WIFI enabled?")
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
        val films = ArrayList<Film>()

        executor.execute{
            var json: JSONObject? = null
            try {
                json = readJsonFromUrl("$BASE_URL/films?search=${search}")
            } catch (err: IOException) {
                Log.w("Network.getFilms", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }

            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val film = parseFilm(item)
                films.add(film)
            }
            handler.post {
                onSuccess(films)
            }
        }
    }

    override fun getPeople(search: String, onSuccess: (people: ArrayList<Person>) -> Unit, onError: (text: String) -> Unit) {
        val people = ArrayList<Person>()

        executor.execute{
            var json: JSONObject? = null
            try {
                json = readJsonFromUrl("$BASE_URL/people?search=${search}")
            } catch (err: IOException) {
                Log.w("Network.getPeople", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }
            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val person = parsePerson(item)
                people.add(person)
            }
            handler.post {
                onSuccess(people)
            }
        }
    }

    override fun getPlanets(search: String, onSuccess: (planets: ArrayList<Planet>) -> Unit, onError: (text: String) -> Unit) {
        val planets = ArrayList<Planet>()

        executor.execute{
            var json: JSONObject? = null
            try {
                json = readJsonFromUrl("$BASE_URL/planets?search=${search}")
            } catch (err: IOException) {
                Log.w("Network.getPlanets", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }

            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val planet = parsePlanet(item)
                planets.add(planet)
            }
            handler.post {
                onSuccess(planets)
            }
        }
    }

    override fun getStarships(search: String, onSuccess: (starships: ArrayList<Starship>) -> Unit, onError: (text: String) -> Unit) {
        val starships = ArrayList<Starship>()

        executor.execute{
            var json: JSONObject? = null
            try {
                json = readJsonFromUrl("$BASE_URL/starships?search=${search}")
            } catch (err: IOException) {
                Log.w("Network.getStarships", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }

            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val starship = parseStarship(item)
                starships.add(starship)
            }
            handler.post {
                onSuccess(starships)
            }
        }
    }

    override fun getSpeciesList(search: String, onSuccess: (speciesList: ArrayList<Species>) -> Unit, onError: (text: String) -> Unit) {
        val speciesList = ArrayList<Species>()

        executor.execute{
            var json: JSONObject? = null
            try {
                json = readJsonFromUrl("$BASE_URL/species?search=${search}")
            } catch (err: IOException) {
                Log.w("Network.getSpeciesList", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }

            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val species = parseSpecies(item)
                speciesList.add(species)
            }
            handler.post {
                onSuccess(speciesList)
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
                    Log.w("Network.getFilmsBy", "No connection...", err)
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
                    Log.w("Network.getPeopleBy", "No connection...", err)
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
                    Log.w("Network.getStarshipsBy", "No connection...", err)
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
                    Log.w("Network.getPlanetsBy", "No connection...", err)
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
                    Log.w("Network.getSpeciesBy", "No connection...", err)
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
}

/** See @url https://stackoverflow.com/a/4308662 */
@Throws(IOException::class, JSONException::class)
private fun readJsonFromUrl(url: String): JSONObject {
    val `is`: InputStream = URL(url).openStream()
    return `is`.use { `is` ->
        val rd = BufferedReader(InputStreamReader(`is`, Charset.forName("UTF-8")))
        val jsonText = readAll(rd)
        JSONObject(jsonText)
    }
}

/** See @url https://stackoverflow.com/a/43086622 */
@Throws(IOException::class)
private fun readAll(rd: Reader): String {
    val sb = StringBuilder()
    var cp: Int
    while (rd.read().also { cp = it } != -1) {
        sb.append(cp.toChar())
    }
    return sb.toString()
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