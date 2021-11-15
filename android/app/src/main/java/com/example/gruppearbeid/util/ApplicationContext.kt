package com.example.gruppearbeid.util

import android.app.Application
import android.content.Context

class ApplicationContext : Application() {
    lateinit var appContext: Context

    init {
        appContext = applicationContext
    }
}