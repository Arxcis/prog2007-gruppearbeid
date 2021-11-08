package com.example.gruppearbeid.adapters

import android.net.ConnectivityManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gruppearbeid.R
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.util.Network


class FilmsAdapter(theActivityRef: AppCompatActivity)
    : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {
    private var films = ArrayList<Film>();

    var ActivityRef: AppCompatActivity = theActivityRef

    init {
        val cm: ConnectivityManager? = ContextCompat.getSystemService(ActivityRef, ConnectivityManager::class.java)
        if (cm !== null)
        {
            Network.connectionMng = cm
            Network.checkInternetConnection()
        }
        Network.getFilms(films, this)
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView? = view.findViewById(R.id.FilmTitle)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_films_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val film = films[position]
        holder.title?.text = film.title
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = films.size
}