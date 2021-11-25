package com.example.gruppearbeid.util

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.Executors

object Storage {

    private val state = Environment.getExternalStorageState()
    private val executor = Executors.newSingleThreadExecutor()

    private val TAG = "Storage.util"

    fun saveImage(bitmap: Bitmap)
    {
        if (Environment.MEDIA_MOUNTED == state)
        {
            Log.d(TAG, "MEdia ins mounted")
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q)   //API level is under 29
            {
                val mainFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file: File = File(mainFolder,"testName.jpg")
                val fileOutput = FileOutputStream(file)

                Log.d(TAG, "thread for compressing and saving image has started")
                executor.execute{
                    try {
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutput)
                        fileOutput.flush()
                        fileOutput.close()
                        Log.d(TAG, "Thread for compressing and saving image has finished.")
                    } catch (ex: IOException) {
                        Log.d(TAG, "Input-output exception occurred")
                    } catch (ex: Exception) {
                        Log.d(TAG, "an exception occurred. Please try again.")
                    }
                }
            }

        }
    }
}