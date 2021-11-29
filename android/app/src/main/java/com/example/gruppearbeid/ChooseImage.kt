package com.example.gruppearbeid

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.gruppearbeid.databinding.ActivityChooseImageBinding
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.INetwork
import com.example.gruppearbeid.util.Network
import com.example.gruppearbeid.util.Storage
import java.util.concurrent.Executors

class ChooseImage : AppCompatActivity() {
    private lateinit var _binding: ActivityChooseImageBinding

    private lateinit var url: String
    private lateinit var requestCode: ActivityResultLauncher<String>
    private lateinit var network: INetwork

    private val executor = Executors.newSingleThreadExecutor()

    private val TAG = "ChooseImageAct"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChooseImageBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        executor.execute {
            val policy: StrictMode.VmPolicy = StrictMode.VmPolicy.Builder().detectLeakedClosableObjects().build()   //for debugging purposes
            StrictMode.setVmPolicy(policy)
        }
        network = Network(this)
        requestCode = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean -> }


        url = intent.getStringExtra(Constants.API_DATA_ENTITY)!!
        url?.let {
            Log.d("chooseImage", "url: ${it}")
        }

        var fileName = url.substring(Constants.PROTOCOL_SWAPI_URL.length, url.lastIndex)            //remove https:// from filename.
                                                                                    //the other "/" in fileName will be replaced with underscore character.
        fileName = fileName.replace("/", "_")                      //"/" gets replaced with "_" in file names
        _binding.btnURLChooseImage.setOnClickListener {

            val urlText = _binding.etURLChooseImage.text.toString()
            network.downloadImage(urlText,
                this, {
                    Log.d(TAG, "happens twice?")
                    _binding.imageChooseImage.setImageBitmap(Storage.bitmap)
                }, fileName, {
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

                }, this
            )

        }

        _binding.btnChooseFinalImage.setOnClickListener {
            val oneUri: String = Storage.findImageFromDirectory(fileName, this).toString()
            Log.d(TAG, "DONE: ${oneUri}")
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