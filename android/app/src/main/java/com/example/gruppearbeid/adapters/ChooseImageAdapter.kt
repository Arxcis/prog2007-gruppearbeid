package com.example.gruppearbeid.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gruppearbeid.R

class ChooseImageAdapter : RecyclerView.Adapter<ChooseImageAdapter.ViewHolder>() {
    var dataFromSWAPI: ArrayList<String> = ArrayList()
    lateinit var whatToFetch: String

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val tvEntity = view.findViewById<TextView>(R.id.tvChooseImageItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_image_item, parent, false)
        return ChooseImageAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvEntity.text = "Test"
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun disp() = Log.d("ChooseImageAda", whatToFetch)
}