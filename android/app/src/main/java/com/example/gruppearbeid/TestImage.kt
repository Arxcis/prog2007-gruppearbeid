package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gruppearbeid.databinding.ActivityTestImageBinding
import com.example.gruppearbeid.util.Network

class TestImage : AppCompatActivity() {
    private lateinit var tImage: ActivityTestImageBinding
    private val URL = "https://image.slidesharecdn.com/7thingsstockimages-140124084729-phpapp01/95/7-types-of-stock-images-you-must-stop-using-today-40-638.jpg?cb=1390828351"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tImage = ActivityTestImageBinding.inflate(layoutInflater)

        Network.downloadImage(URL, tImage.image)

        setContentView(tImage.root)
    }
}