package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.databinding.ActivityChooseImageBinding

class ChooseImage : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var _binding: ActivityChooseImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChooseImageBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        //choose adapter which holds data of different entries
        val spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.choices_select_image,android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        _binding.spinnerListEntities.adapter = spinnerAdapter

        _binding.spinnerListEntities.onItemSelectedListener = this        //link SpinnerActivity to the onSelectedListener
                                                                                       //of spinner.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("ChooseImageActi", parent?.getItemAtPosition(position).toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}