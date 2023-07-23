package com.example.todoapp.ui.ViewModel

import androidx.lifecycle.*
import com.example.todoapp.data.Repository.StartRepository
import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditTaskViewModel @Inject constructor(
    var repository: StartRepository
) : ViewModel() {

    // Counter of completed tasks
    var completedTasks: MutableLiveData<Int> = repository.completedTasks


    // Add task in Room
    fun addTask(todoItem: TodoItem){
        repository.addTask(todoItem)
    }

    // Remove task from Room
    fun removeTask(todoItem: TodoItem){
        viewModelScope.launch {
            repository.removeTask(todoItem)
        }
    }

    // Update task in Room
    fun updateTask(todoItem: TodoItem){
        viewModelScope.launch {
            repository.updateTask(todoItem)
        }
    }

    // Update task in Adapter
    fun updateTaskInAdapter(todoItem: TodoItem){
        repository.updateTaskInAdapter(todoItem)
    }

    // Clear current editing model
    fun clearCurrModel(){
        repository.clearCurrModel()
    }

    // Get current editing model
    fun getCurrModel() = repository.getCurrModel()

    // Is currently editing
    fun isCurrEditing() = repository.isCurrEditing()

    // Clear currently editing
    fun clearCurrEditing() {
        repository.clearCurrEditing()
    }

    // Get month by index
    fun getMonthByIndex(index: Int): String{
        return repository.months[index]
    }

    // Clear currently chosen deadline
    fun clearDeadline(){
        repository.getCurrModel()!!.deadlineData =  ""
    }

    // Get current day, month and year
    fun getCurrentDate(): List<Int> {
        return repository.getCurrentDate()
    }

    // Increase/decrease counter of completed tasks
    fun increaseCompletedTasks(delta: Int){
        completedTasks.value = completedTasks.value?.plus(delta)
    }

}