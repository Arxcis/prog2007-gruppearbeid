package com.example.gruppearbeid

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.AdapterView

class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {
    lateinit var itemSelected: String
    private val TAG = "ChooseImage.Spinner"

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        itemSelected = parent?.getItemAtPosition(position).toString()
        Log.d(TAG, "item selected ${itemSelected}")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}