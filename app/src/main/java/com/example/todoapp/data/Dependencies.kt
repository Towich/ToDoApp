package com.example.todoapp.data

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.db.AppDatabase
import com.example.todoapp.data.db.TaskDao
import com.example.todoapp.ui.Repository.StartRepository

object Dependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context){
        applicationContext = context
    }

    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    val startRepository: StartRepository by lazy { StartRepository(appDatabase.TaskDao()) }
}