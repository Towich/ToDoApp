package com.example.todoapp.ui.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.App
import com.example.todoapp.data.Repository.StartRepository
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var repository: StartRepository

    private var currModel: TodoItem? = null
    private var isCurrEditing: Boolean? = null // Is current editing or creating a new work

    var completedTasks: MutableLiveData<Int>
    var showingUncompletedTasks: Boolean = false

    init {
        // Injecting this ViewModel
        (application as App).appComponent.inject(this)
        completedTasks = repository.completedTasks
    }

    // Tasks
    fun addTask(todoItem: TodoItem){
        repository.addTask(todoItem)
    }

    fun setupTasks() {
        viewModelScope.launch {
            val tasksList = repository.getTasks()
            repository.setAllTasks(tasksList)
        }
    }

    fun setupUncompletedTasks(){
        viewModelScope.launch {
            val tasksList = repository.getAllUncompletedTasks()

            repository.setAllTasks(tasksList)
        }

    }

    fun setupQuantityOfCompletedTasks(){
        viewModelScope.launch {
            val tasksList = repository.getAllCompletedTasks()

            completedTasks.value = tasksList.size
        }
    }

    fun removeTask(todoItem: TodoItem){
        viewModelScope.launch {
            repository.removeWork(todoItem)
        }
    }
    fun updateTask(todoItem: TodoItem){
        viewModelScope.launch {
            repository.updateTask(todoItem)
        }
    }

    fun updateTaskInAdapter(todoItem: TodoItem){
        repository.updateTaskInAdapter(todoItem)
    }

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

    fun increaseCompletedTasks(delta: Int){
        completedTasks.value = completedTasks.value?.plus(delta)
    }

}