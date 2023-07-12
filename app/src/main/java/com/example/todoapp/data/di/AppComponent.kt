package com.example.todoapp.data.di

import android.content.Context
import com.example.todoapp.data.network.NetworkModule
import dagger.BindsInstance
import dagger.Component

/**
 * Main Component of Application.
 */
@ApplicationScope
@Component(modules = [StorageModule::class, NetworkModule::class])
interface AppComponent {

    // Factory to create an instance of AppComponent
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    // Classes that be injected by this Component
    fun startComponent(): FragmentComponent.Factory
}