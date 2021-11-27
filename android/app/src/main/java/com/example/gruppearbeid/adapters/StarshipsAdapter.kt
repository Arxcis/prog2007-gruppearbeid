package com.example.gruppearbeid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gruppearbeid.R
import com.example.gruppearbeid.types.Starship

class StarshipsAdapter(
    private val onClick: (starship: Starship) -> Unit
) : RecyclerView.Adapter<StarshipsAdapter.ViewHolder>() {
    private var things = ArrayList<Starship>()

    fun refresh(newThings: List<Starship>) {
        things.clear()
        things.addAll(newThings)
        this.notifyDataSetChanged()
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView? = view.findViewById(R.id.StarshipName)
        val item: View? = view.findViewById(R.id.AdapterStarshipsItem)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_starships_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val starship = things[position]

        holder.name?.text = "🚀 ${starship.name}"
        holder.item?.setOnClickListener { onClick(starship) }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = things.size
}