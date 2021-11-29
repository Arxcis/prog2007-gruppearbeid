package com.example.gruppearbeid.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
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
import androidx.core.net.toUri
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.net.URI
import java.util.concurrent.Executors

object Storage {

    private val state = Environment.getExternalStorageState()
    private val executor = Executors.newSingleThreadExecutor()

    private var doneSavingImage: Boolean = false
    lateinit var bitmap: Bitmap

    private lateinit var lastSavedImageUri: Uri
    private val TAG = "Storage.util"

    private val theBaseUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    @RequiresApi(Build.VERSION_CODES.P)
    fun saveImage(bitmap: Bitmap, fileName: String, permission: () -> Boolean, appContext: Context, updateImage: () -> Unit,
                  onError: (errText: String) -> Unit)
    {
        doneSavingImage = false
        if (Environment.MEDIA_MOUNTED == state)
        {
            Log.d(TAG, "MEdia ins mounted")
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.Q)   //API level is  29 higher
            {
                if (permission())
                {  //if have permissiion to write to external storage.

                    val values: ContentValues = ContentValues().apply {
                        put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "${fileName}.jpg")
                    }

                    try {
                        val resolver = appContext.contentResolver
                        lastSavedImageUri = resolver.insert(theBaseUri, values)!!
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
                            doneSavingImage = true
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
                }
            }
        }
    }

    fun findImageFromDirectory(fileName: String, context: Context) : Uri? {
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME)

        val activity: Activity? = context as? Activity
        activity?.let {

            val cursor: Cursor? = activity.contentResolver.query(theBaseUri, projection, null, null,null)

            cursor?.let {
                it.moveToFirst()
                do {
                    try {
                        val columnIndexID = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                        val imageID = it.getLong(columnIndexID)
                        val uriImage = Uri.withAppendedPath(theBaseUri, "" + imageID)
                        val currentFileName = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))

                        Log.d(TAG, "fileName: ${currentFileName}")
                        Log.d(TAG, uriImage.toString())
                        if (currentFileName.contains(fileName))
                        {
                            return uriImage
                        }

                    }catch (ex: IllegalArgumentException)
                    {
                        Log.d(TAG, "${ex.message}")
                    }
                } while(it.moveToNext())
            }
            cursor?.close()
        }
        return null         //return null if didn't find title or activity or cursor is null
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun displayImage(context: Context, updateImage: () -> Unit, uri: Uri) {
        try {
            val imgDecoder: ImageDecoder.Source = ImageDecoder.createSource(context.contentResolver, uri)
            bitmap = ImageDecoder.decodeBitmap(imgDecoder)

            val activity: Activity? = context as? Activity
            activity?.let {                     //try to update the bitmap with one found from external storage
                it.runOnUiThread(object: Runnable {
                    override fun run() {
                        updateImage()
                    }
                })
            }

        } catch(ex: IOException)
        {
            Log.d(TAG, "${ex.message}")
        }
    }

    fun parseURL(url: String) : String {
        var fileName = url.substring(Constants.PROTOCOL_SWAPI_URL.length, url.lastIndex)            //remove https:// from filename.
        //the other "/" in fileName will be replaced with underscore character.
        fileName = fileName.replace("/", "_")                      //"/" gets replaced with "_" in file names
        return fileName
    }
}
