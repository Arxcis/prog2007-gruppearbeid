package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.ChooseImageAdapter
import com.example.gruppearbeid.databinding.ActivityChooseImageBinding

class ChooseImage : AppCompatActivity() {
    private lateinit var _binding: ActivityChooseImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChooseImageBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        //choose adapter which holds data of different entries
        val spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.choices_select_image,android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        _binding.spinnerListEntities.adapter = spinnerAdapter

        val spinnerActivity = SpinnerActivity()
        _binding.spinnerListEntities.onItemSelectedListener = spinnerActivity        //link SpinnerActivity to the onSelectedListener
                                                                                       //of spinner.

        val adapter = ChooseImageAdapter()

        _binding.rvChooseImage.adapter = adapter
        _binding.rvChooseImage.layoutManager = LinearLayoutManager(this)
    }
}