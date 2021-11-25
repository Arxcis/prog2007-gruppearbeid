package com.example.gruppearbeid.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.adapters.StarshipsAdapter
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.types.Starship
import java.util.concurrent.Executors
import org.json.JSONObject

import org.json.JSONException
import java.io.*
import java.lang.StringBuilder
import java.net.URL
import java.nio.charset.Charset


object Network {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private val BASE_URL = "https://swapi.dev/api"

    fun getFilms(films: ArrayList<Film>, adapter: FilmsAdapter, onError: (text: String) -> Unit) {
        executor.execute{
            var json: JSONObject? = null
            try {
                json = readJsonFromUrl("$BASE_URL/films")
            } catch (err: IOException) {
                Log.w("Network.getFilms", "No connection...", err)
                handler.post { onError("No connection...") }
                return@execute
            }

            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)

                val characters = ArrayList<String>()
                val jsonCharacters = item.getJSONArray("characters")
                for (k in 0 until jsonCharacters.length()) {
                    characters.add(jsonCharacters.get(k).toString())
                }

                val film = Film(
                    title = item.getString("title"),
                    characters = characters,
                )
                films.add(film)
            }
            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun getPeople(people: ArrayList<Person>, adapter: PeopleAdapter, onError: (text: String) -> Unit) {
        executor.execute{
            var json: JSONObject? = null
            try {
                json = readJsonFromUrl("$BASE_URL/people")
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
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun getPlanets(planets: ArrayList<Planet>, adapter:PlanetsAdapter, onError: (text: String) -> Unit) {
        executor.execute{
            var json: JSONObject? = null
            try {
                json = readJsonFromUrl("$BASE_URL/planets")
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
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun getStarships(starships: ArrayList<Starship>, adapter: StarshipsAdapter, onError: (text: String) -> Unit) {
        executor.execute{
            var json: JSONObject? = null
            try {
                json = readJsonFromUrl("$BASE_URL/starships")
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
                adapter.notifyDataSetChanged()
            }
        }
    }


    fun getFilmsByURL(urls: ArrayList<String>, films: ArrayList<Film>, adapter: FilmsAdapter, onError: (text: String) -> Unit) {
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

                val characters = ArrayList<String>()
                val jsonCharacters = json.getJSONArray("characters")
                for (k in 0 until jsonCharacters.length()) {
                    characters.add(jsonCharacters.get(k).toString())
                }

                val film = Film(
                    title = json.getString("title"),
                    characters = characters
                )
                films.add(film)
            }

            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun getPeopleByURL(urls: ArrayList<String>, people: ArrayList<Person>, adapter: PeopleAdapter, onError: (text: String) -> Unit) {
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

    fun getStarshipsByURL(urls: ArrayList<String>, starships: ArrayList<Starship>, adapter: StarshipsAdapter, onError: (text: String) -> Unit) {
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

    fun getPlanetsByUrl(urls: ArrayList<String>, planets: ArrayList<Planet>, adapter: PlanetsAdapter, onError: (text: String) -> Unit) {
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

    return Planet(
        name = item.getString("name"),
        residents = residents
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

    return Person(
        name = item.getString("name"),
        homeworld = homeworld,
        films = films,
        starships = starships,
    )
}