package com.example.gruppearbeid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gruppearbeid.R
import com.example.gruppearbeid.types.Person


class PeopleAdapter(
) : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {
    private var people = ArrayList<Person>();

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_people_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = people[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = people.size
}