package com.example.todoapp.data.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.ui.ViewModel.EditTaskViewModel
import com.example.todoapp.ui.ViewModel.StartViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule
{
    // this needs to be only one for whole app (therefore marked as `@Singleton`)
    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory


    // for every ViewModel you have in your app, you need to bind them to dagger
    @Binds
    @IntoMap
    @ViewModelKey(StartViewModel::class)
    abstract fun bindStartViewModel(vm: StartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditTaskViewModel::class)
    abstract fun bindEditTaskViewModel(vm: EditTaskViewModel): ViewModel

}