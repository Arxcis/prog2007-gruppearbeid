package com.example.gruppearbeid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gruppearbeid.R
import com.example.gruppearbeid.types.Film
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.util.Network


class PeopleAdapter(
    private val onClick: (film: Person) -> Unit
) : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {
    private var people = ArrayList<Person>();
    init {
        Network.getPeople(people, this)
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView? = view.findViewById(R.id.PersonName)
        val item: View? = view.findViewById(R.id.AdapterPeopleItem)
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
        holder.name?.text = person.name
        holder.item?.setOnClickListener { onClick(person) }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = people.size
}