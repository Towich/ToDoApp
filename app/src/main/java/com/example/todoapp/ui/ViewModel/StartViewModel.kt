package com.example.todoapp.ui.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import com.example.todoapp.ui.Repository.StartRepository

class StartViewModel: ViewModel() {

    private val repository = StartRepository()
    private var works: MutableLiveData<List<TodoItem>> = repository.getWorks()
    private var uncompletedTasks: MutableLiveData<List<TodoItem>> = repository.getUncompletedTasks()

    private var currModel: TodoItem? = null
    private var isCurrEditing: Boolean? = null // Is current editing or creating a new work

    private var completedTasks: Int = 0

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
    fun removeWork(todoItem: TodoItem, position: Int){
        repository.removeWork(position)

        if(!todoItem.completed)
            repository.removeUncompletedTask(todoItem)
    }
    fun getWork(index: Int) = repository.getTask(index)

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

    fun getAdapter(): CustomRecyclerAdapter = repository.getAdapter()

    fun setUncompletedTasks() = repository.setUncompletedTasks()

    fun setAllTasks() = repository.setAllTasks()

    fun getCompletedTasks() = completedTasks

    fun increaseCompletedTasks(delta: Int){
        completedTasks += delta
    }

}