package com.example.new_gymsarround_app

import android.app.Application
import android.content.Context

class GymsApplication:Application() {
    init{
        application = this
    }
    companion object{
    private lateinit var application:GymsApplication
    fun getApplicationContext():Context = application.applicationContext
    }
}