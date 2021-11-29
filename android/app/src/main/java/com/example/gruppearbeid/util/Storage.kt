package com.example.gruppearbeid.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.BoolRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.lang.NullPointerException
import java.util.concurrent.Executors

object Storage {

    private val state = Environment.getExternalStorageState()
    private val executor = Executors.newSingleThreadExecutor()

    private lateinit var lastSavedImageUri: Uri

    private val TAG = "Storage.util"

    @RequiresApi(Build.VERSION_CODES.P)
    fun saveImage(bitmap: Bitmap, fileName: String, permission: () -> Boolean, appContext: Context, updateImage: () -> Unit)
    {
        if (Environment.MEDIA_MOUNTED == state)
        {
            Log.d(TAG, "MEdia ins mounted")
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q)   //API level is under 29
            {
                if (permission())
                {  //if have permissiion to write to external storage.

                    val values: ContentValues = ContentValues().apply {
                        put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "${fileName}.jpg")
                    }

                    val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                    try {
                        val resolver = appContext.contentResolver
                        lastSavedImageUri = resolver.insert(imageUri, values)!!
                        lastSavedImageUri?.let {
                            val output = resolver.openOutputStream(lastSavedImageUri)
                            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)) //if compress was successful
                            {
                                Log.d(TAG, "compressing was successful")
                            }else {
                                Log.d(TAG, "not able to compress bitmap to external storage")
                            }

                            output?.flush()
                            output?.close()
                            fetchImage(appContext, updateImage)
                        }
                        if (lastSavedImageUri == null) {
                            Log.d(TAG, "content resolver is null")
                        }

                    }catch (ex: Exception) {
                        Log.d(TAG, "exception: ${ex.message}")
                    } catch(ex:FileNotFoundException)
                    {
                        Log.d(TAG, "could not find the provided file URI.") //from ContentResolver.openOutputStream()
                    }

/*
                    Log.d(TAG, "thread for compressing and saving image has started")
                    executor.execute {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutput)
                            fileOutput.flush()
                            fileOutput.close()
                            Log.d(TAG, "Thread for compressing and saving image has finished.")
                        } catch (ex: IOException) {
                            Log.d(TAG, "Input-output exception occurred")
                        } catch (ex: Exception) {
                            Log.d(TAG, "an exception occurred. Please try again.")
                            Log.d(TAG, "${ex.message}")
                        }*/
                    }
                }
            }
        }

    fun listFilesDirectory() {
        try {
            val uriStorage: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val fileObject = File(uriStorage.path)

            val filesInDirectory = fileObject.listFiles()  //use this, loop through this.

            filesInDirectory?.let {
                Log.d(TAG, "directory path not null")
                for (i in filesInDirectory.indices)
                {
                    Log.d(TAG, "${filesInDirectory[i]}")
                }
            }

        }catch(ex: NullPointerException)
        {
            Log.d(TAG, "${ex.message}")
            Log.d(TAG, "null exception")
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun fetchImage(context: Context, updateImage: () -> Unit) {
        try {
            lastSavedImageUri?.let {
                val imgDecoder: ImageDecoder.Source = ImageDecoder.createSource(context.contentResolver, lastSavedImageUri)
                val bitmap = ImageDecoder.decodeBitmap(imgDecoder)

                val activity: Activity? = context as? Activity
                activity?.let {                     //try to update the bitmap with one found from external storage
                    it.runOnUiThread(object: Runnable {
                        override fun run() {
                            updateImage()
                        }
                    })
                }
            }
        } catch(ex: IOException)
        {
            Log.d(TAG, "${ex.message}")
        }


    }
}
