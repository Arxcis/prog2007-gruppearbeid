package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.databinding.ActivityChooseImageBinding
import com.example.gruppearbeid.util.Constants

class ChooseImage : AppCompatActivity() {
    private lateinit var _binding: ActivityChooseImageBinding

    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChooseImageBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        url = intent.getStringExtra(Constants.API_DATA_ENTITY)!!
        url?.let {
            Log.d("chooseImage", "url: ${it}")
        }
    }
}