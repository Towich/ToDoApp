package com.example.todoapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.database.AppDatabase
import com.example.todoapp.data.database.TaskDao
import dagger.Module
import dagger.Provides

@Module
class StorageModule {

    @Provides
    fun provideAppDatabase(mApplicationContext: Context): AppDatabase {
        return Room.databaseBuilder(mApplicationContext, AppDatabase::class.java, "app-database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDao(mApplicationContext: Context): TaskDao {
        return Room.databaseBuilder(mApplicationContext, AppDatabase::class.java, "app-database.db")
            .fallbackToDestructiveMigration()
            .build()
            .TaskDao()
    }
}