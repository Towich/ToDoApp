package com.example.todoapp.data.di

import com.example.todoapp.ui.Fragment.EditTaskFragment
import com.example.todoapp.ui.Fragment.EditWorkFragment
import com.example.todoapp.ui.Fragment.StartFragment
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [ViewModelModule::class])
interface FragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }

    // Classes that be injected by this Component
    fun inject(fragment: StartFragment)
    fun inject(fragment: EditWorkFragment)
    fun inject(fragment: EditTaskFragment)
}