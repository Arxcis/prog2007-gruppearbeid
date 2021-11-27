package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.ChooseImageAdapter
import com.example.gruppearbeid.databinding.ActivityChooseImageBinding
import com.example.gruppearbeid.types.myViewModel

class ChooseImage : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var _binding: ActivityChooseImageBinding
    private lateinit var adapter: ChooseImageAdapter

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

        adapter = ChooseImageAdapter()

        _binding.rvChooseImage.adapter = adapter
        _binding.rvChooseImage.layoutManager = LinearLayoutManager(this)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        adapter.whatToFetch = parent?.getItemAtPosition(position).toString()
        adapter.disp()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}