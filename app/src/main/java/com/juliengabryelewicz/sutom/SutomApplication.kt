package com.juliengabryelewicz.sutom

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SutomApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}