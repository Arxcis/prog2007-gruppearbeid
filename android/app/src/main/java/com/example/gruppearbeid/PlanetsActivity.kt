package com.example.gruppearbeid

import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PlanetsAdapter
import com.example.gruppearbeid.databinding.ActivityPlanetsBinding
import com.example.gruppearbeid.types.Planet
import com.example.gruppearbeid.util.*
import kotlinx.android.synthetic.main.activity_planets.*
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.makeTextWatcherWithDebounce
import com.example.gruppearbeid.util.navigateToThing
import java.util.jar.Manifest

class PlanetsActivity : AppCompatActivity() {
    private lateinit var network: INetwork

    private lateinit var requestCode: ActivityResultLauncher<String>
    val URL = "https://image.slidesharecdn.com/7thingsstockimages-140124084729-phpapp01/95/7-types-of-stock-images-you-must-stop-using-today-40-638.jpg?cb=1390828351"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planets)
        title = getString(R.string.planets)

        requestCode = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        }

        val adapter = PlanetsAdapter{ planet ->
            navigateToThing(this, PlanetActivity::class.java, planet)
        }
        PlanetRecycler.adapter = adapter
        PlanetRecycler.layoutManager = LinearLayoutManager(this)

        // 2. Init search
        network = Network(this)
        val search = { text: String ->
            network.searchPlanets(
                search = text,
                onSuccess = { res -> adapter.refresh(res.results) },
                onError = { error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
            )
        }
        search("")
        PlanetsSearch.addTextChangedListener(
            makeTextWatcherWithDebounce{ input -> search(input)}
        )

        network.downloadImage(URL, this, {
            val image: ImageView = findViewById<ImageView>(R.id.imagePlanets)
            image.setImageBitmap(network.bitmap)
        },this::checkPermission, applicationContext)
        // 1. Init adapter
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PlanetsNavigation, R.id.PlanetsMenuItem)
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