package com.example.gruppearbeid.util

import android.content.Context


interface ICache {
    fun getValue(key: String): String;
    fun setValue(key: String, value: String);
}

class SimpleCache(private val ctx: Context) : ICache {
    override fun getValue(key: String): String {
        TODO("Not yet implemented")
    }

    override fun setValue(key: String, value: String) {
        TODO("Not yet implemented")
    }
}