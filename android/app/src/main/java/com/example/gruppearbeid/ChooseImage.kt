package com.example.gruppearbeid

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.databinding.ActivityChooseImageBinding
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.INetwork
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.Storage

class ChooseImage : AppCompatActivity() {
    private lateinit var _binding: ActivityChooseImageBinding

    private lateinit var url: String
    private lateinit var requestCode: ActivityResultLauncher<String>
    private lateinit var network: INetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChooseImageBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        network = Network(this)
        requestCode = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean -> }


        url = intent.getStringExtra(Constants.API_DATA_ENTITY)!!
        url?.let {
            Log.d("chooseImage", "url: ${it}")
        }

        _binding.btnURLChooseImage.setOnClickListener {

            val urlText = _binding.etURLChooseImage.text.toString()
            network.downloadImage(urlText,
                this, {
                _binding.imageChooseImage.setImageBitmap(network.bitmap)
            }, {
                    if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    return@downloadImage true
                } else {
                    requestCode.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    return@downloadImage true
                }
                    return@downloadImage false

                }, this)

        }
    }

    fun checkPermission() : Boolean{
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        else {
            requestCode.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return true
        }
        return false
    }
}