package com.example.gruppearbeid.util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkMngSample(appContext: Context, workParams: WorkerParameters) :
    Worker(appContext, workParams) {
    override fun doWork(): Result {
        Thread.sleep(5000)
        Log.d("WOrkMgSample", "yeah boi")
        return Result.success()
    }


}