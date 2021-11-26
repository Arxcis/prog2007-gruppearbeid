package com.example.gruppearbeid.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors


interface ICache {
    fun getValue(key: String?, defaultValue: String?): String?
    fun setValue(key: String?, value: String?)
}

class SimpleCache(ctx: Context, cacheName: String) : ICache {
    private val sharedPrefs = ctx.getSharedPreferences(cacheName, Context.MODE_PRIVATE)
    private val executor = Executors.newSingleThreadExecutor()

    override fun getValue(key: String?, default: String?): String? {
        if (key == null) {
            return null
        }
        return sharedPrefs.getString(key, default)
    }

    override fun setValue(key: String?, value: String?) {
        if (key == null) {
            return
        }
        executor.execute {
            with(sharedPrefs.edit()) {
                putString(key, value)
                apply()
            }
        }
    }
}