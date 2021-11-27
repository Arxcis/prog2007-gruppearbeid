package com.example.gruppearbeid.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gruppearbeid.R
import com.example.gruppearbeid.util.INetwork
import com.example.gruppearbeid.util.Network

class ChooseImageAdapter(context: Context) : RecyclerView.Adapter<ChooseImageAdapter.ViewHolder>() {
    var dataFromSWAPI: ArrayList<String> = ArrayList()
    lateinit var whatToFetch: String
    private var contextRef: Context? = context

    lateinit var network: INetwork

    val getTheFilms = { text: String ->
        network.getFilms(text,
            onSuccess = { _films ->
                for (i in _films.indices)
                {
                    dataFromSWAPI.add(_films[i].title)
                }
                dispAll(dataFromSWAPI)
            }, onError = { error ->
                Toast.makeText(contextRef, error, Toast.LENGTH_LONG)
            })
    }

    private val TAG = "ChooseImageAd"

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val tvEntity = view.findViewById<TextView>(R.id.tvChooseImageItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_image_item, parent, false)
        contextRef?.let {
            network = Network(it)
        }
        return ChooseImageAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvEntity.text = "test"
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun fetchData() {
       when(whatToFetch)
        {
            "People" ->
           getTheFilms("")
        }
    }

    fun onDestroy()
    {
        contextRef = null
    }

    fun dispAll(list: ArrayList<String>)
    {
        for (i in list.indices)
        {
            Log.d(TAG, list[i])
        }
    }

}