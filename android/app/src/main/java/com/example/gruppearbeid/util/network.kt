package com.example.gruppearbeid.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.gruppearbeid.adapters.FilmsAdapter
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
    fun getPeople(): ArrayList<Person> = ArrayList<Person>()

    @Throws(IOException::class, JSONException::class)
    fun getPlanets(): ArrayList<Planet> = ArrayList<Planet>()

    @Throws(IOException::class, JSONException::class)
    fun getStarships(): ArrayList<Starship> = ArrayList<Starship>()


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