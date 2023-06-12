package com.example.todoapp.ui.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Repository.StartRepository

class StartViewModel: ViewModel() {

    private val repository = StartRepository()
    private var works: MutableLiveData<List<TodoItem>> = repository.getWorks()

    fun getWorks() = works

    fun addWork(work: TodoItem){
        repository.addWork(work)
    }
}