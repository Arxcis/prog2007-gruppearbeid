package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.gruppearbeid.util.WorkManagerImage
import java.util.concurrent.TimeUnit

class DownloadImage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_image)
    }

    fun downloadImage(view: View)
    {
        val downImageRequest = OneTimeWorkRequestBuilder<WorkManagerImage>()
            .setBackoffCriteria(
            BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()
        WorkManager.getInstance(this).enqueue(downImageRequest)
    }
}