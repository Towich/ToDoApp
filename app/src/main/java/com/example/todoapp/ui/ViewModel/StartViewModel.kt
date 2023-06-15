package com.example.todoapp.ui.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Repository.StartRepository

class StartViewModel: ViewModel() {

    private val repository = StartRepository()
    private var works: MutableLiveData<List<TodoItem>> = repository.getWorks()

    private var currModel: TodoItem? = null
    private var isCurrEditing: Boolean? = null // Is current editing or creating a new work

    // Works
    fun getWorks() = works
    fun addWork(work: TodoItem){
        repository.addWork(work)
    }
    fun removeWork(todoItem: TodoItem){
        repository.removeWork(todoItem)
    }
    fun getWorkById() = works // TODO
    fun getWork(index: Int) = works.value?.get(index)

    // Current model
    fun setCurrModel(newModel: TodoItem){
        currModel = newModel
    }
    fun clearCurrModel(){
        currModel = null
    }
    fun getCurrModel() = currModel

    // isCurrentEditing

    fun setCurrEditing(newState: Boolean){
        isCurrEditing = newState
    }
    fun isCurrEditing() = isCurrEditing
    fun clearCurrEditing(){
        isCurrEditing = null
    }


}