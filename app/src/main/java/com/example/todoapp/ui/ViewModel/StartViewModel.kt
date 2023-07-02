package com.example.todoapp.ui.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Dependencies
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class StartViewModel: ViewModel() {

    private val repository by lazy { Dependencies.startRepository }

    private var currModel: TodoItem? = null
    private var isCurrEditing: Boolean? = null // Is current editing or creating a new work

    private var completedTasks: Int = 0


    // Tasks
    fun addTask(todoItem: TodoItem){
        repository.addTask(todoItem)
    }

    fun getTasks() {
        viewModelScope.launch {
            val tasksList = repository.getTasks()
            repository.setAllTasks(tasksList)
        }
    }
    fun removeTask(todoItem: TodoItem){
        viewModelScope.launch {
            repository.removeWork(todoItem)
        }


//        if(!todoItem.completed)
//            repository.removeUncompletedTask(todoItem)
    }

    fun updateTask(todoItem: TodoItem){
        viewModelScope.launch {
            repository.updateTask(todoItem)
        }
    }

    fun updateTaskInAdapter(todoItem: TodoItem){
        repository.updateTaskInAdapter(todoItem)
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

//    fun setUncompletedTasks() = repository.setUncompletedTasks()

//    fun setAllTasks() = repository.setAllTasks()

    fun getCompletedTasks() = completedTasks

    fun increaseCompletedTasks(delta: Int){
        completedTasks += delta
    }

}