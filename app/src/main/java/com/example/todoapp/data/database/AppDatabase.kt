package com.example.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun TaskDao(): TaskDao
}