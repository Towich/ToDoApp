package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.di.DaggerAppComponent
import com.example.todoapp.data.di.AppComponent

class App: Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}