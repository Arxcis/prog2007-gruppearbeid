package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gruppearbeid.databinding.ActivityTestImageBinding

class TestImage : AppCompatActivity() {
    private lateinit var tImage: ActivityTestImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tImage = ActivityTestImageBinding.inflate(layoutInflater)
        setContentView(tImage.root)
    }
}