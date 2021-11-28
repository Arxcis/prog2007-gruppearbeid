package com.example.gruppearbeid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gruppearbeid.R
import com.example.gruppearbeid.types.Film


class FilmsAdapter(
    private val onClick: (film: Film) -> Unit,
) : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {
    private var things = ArrayList<Film>()

    fun refresh(newThings: List<Film>) {
        things.clear()
        things.addAll(newThings)
        this.notifyDataSetChanged()
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView? = view.findViewById(R.id.FilmTitle)
        val item: View? = view.findViewById(R.id.AdapterFilmsItem)
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
        val film = things[position]
        holder.title?.text = "🎬 ${film.title}"
        holder.item?.setOnClickListener { onClick(film) }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = things.size
}