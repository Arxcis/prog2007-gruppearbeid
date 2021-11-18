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
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL
import java.nio.charset.Charset


object Network {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private val BASE_URL = "https://swapi.dev/api"

    @Throws(IOException::class, JSONException::class)
    fun getFilms(films: ArrayList<Film>, adapter: FilmsAdapter) {
        executor.execute{
            val json = readJsonFromUrl("$BASE_URL/films")

            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val film = Film(title = item.getString("title"))
                films.add(film)
            }
            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }

    @Throws(IOException::class, JSONException::class)
    fun getPeople(people: ArrayList<Person>, adapter: PeopleAdapter) {
        executor.execute{
            val json = readJsonFromUrl("$BASE_URL/people")

            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val person = Person(name = item.getString("name"))
                people.add(person)
            }
            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }

    @Throws(IOException::class, JSONException::class)
    fun getPlanets(planets: ArrayList<Planet>, adapter:PlanetsAdapter) {
        executor.execute{
            val json = readJsonFromUrl("$BASE_URL/planets")

            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val planet = Planet(name = item.getString("name"))
                planets.add(planet)
            }
            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }

    @Throws(IOException::class, JSONException::class)
    fun getStarships(starships: ArrayList<Starship>, adapter: StarshipsAdapter) {
        executor.execute{
            val json = readJsonFromUrl("$BASE_URL/starships")

            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)

                val starship = Starship(
                    name = item.getString("name"),
                    model = item.getString("model"),
                    manufacturer = item.getString("manufacturer"),
                    length = item.getString("length"),
                    max_atmosphering_speed = item.getString("max_atmosphering_speed"),
                    crew = item.getString("crew"),
                    passengers = item.getString("passengers"),
                    starship_class = item.getString("starship_class")
                )

                starships.add(starship)
            }
            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }


    /** See @url https://stackoverflow.com/a/4308662 */
    @Throws(IOException::class, JSONException::class)
    fun readJsonFromUrl(url: String): JSONObject {
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
}