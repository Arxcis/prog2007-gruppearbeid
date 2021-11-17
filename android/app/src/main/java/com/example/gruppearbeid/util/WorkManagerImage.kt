package com.example.gruppearbeid.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkManagerImage(appContext: Context, workParams: WorkerParameters) :
    Worker(appContext, workParams) {

    private val myContext = appContext
    private val TAG = "WorkManagerImage"

    private val MbpsMinSpeed = 2

    override fun doWork(): Result {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val connectionManager =
                myContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkCapability =
                connectionManager.getNetworkCapabilities(connectionManager.activeNetwork)
            var downloadSpeed = (networkCapability?.linkDownstreamBandwidthKbps)?.div(1000)
            Log.d(TAG, "MBPS: ${downloadSpeed.toString()}")
            
            if (downloadSpeed < MbpsMinSpeed)
            {
                return Result.retry()
            }
            return Result.success()     //return success if fast enough network speed.
        }
        return Result.success()
    }


}