package com.example.gruppearbeid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gruppearbeid.R
import com.example.gruppearbeid.types.Species

class SpeciesListAdapter(
    private var speciesList: ArrayList<Species>,
    private val onClick: (species: Species) -> Unit
) : RecyclerView.Adapter<SpeciesListAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView? = view.findViewById(R.id.SpeciesName)
        val item: View? = view.findViewById(R.id.AdapterSpeciesListItem)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_species_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val species = speciesList[position]

        holder.name?.text = "🧬 ${species.name}"
        holder.item?.setOnClickListener { onClick(species) }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = speciesList.size
}