package com.example.todoapp.data.di

import android.content.Context
import com.example.todoapp.ui.ViewModel.StartViewModel
import dagger.BindsInstance
import dagger.Component

/**
 * Main Component of Application.
 */
@Component(modules = [StorageModule::class])
interface AppComponent {

    // Factory to create an instance of AppComponent
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    // Classes that be injected by this Component
    fun inject(viewModel: StartViewModel)
}