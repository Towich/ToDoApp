package com.example.todoapp.ui.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Repository.StartRepository

class StartViewModel: ViewModel() {

    private val repository = StartRepository()
    private var works: MutableLiveData<List<TodoItem>> = repository.getWorks()
    private var uncompletedTasks: MutableLiveData<List<TodoItem>> = repository.getUncompletedTasks()

    private var currModel: TodoItem? = null
    private var isCurrEditing: Boolean? = null // Is current editing or creating a new work

    // Tasks
    fun getWorks() = works

    fun getUncompletedTasks() = uncompletedTasks
    fun addWork(work: TodoItem){
        repository.addWork(work)
    }
    fun addUncompletedTask(task: TodoItem){
        repository.addUncompletedTask(task)
    }
    fun removeUncompletedTask(task: TodoItem){
        repository.removeUncompletedTask(task)
    }
    fun getSizeCompletedTasks(): Int = repository.getSizeCompletedTasks()
    fun removeWork(todoItem: TodoItem){
        repository.removeWork(todoItem)

        if(!todoItem.completed)
            repository.removeUncompletedTask(todoItem)
    }
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
    fun getMonthByIndex(index: Int): String{
        return repository.MONTHS[index]
    }
    fun clearDeadline(){
        currModel?.deadlineData = ""
    }

    fun getCurrentDate(): List<Int> {
        return repository.getCurrentDate()
    }

}