package com.example.todoapp.ui.ViewModel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.example.todoapp.data.Repository.StartRepository
import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditTaskViewModel @Inject constructor(
    var repository: StartRepository
) : ViewModel() {

    // Counter of completed tasks
    private var completedTasks: MutableLiveData<Int> = repository.completedTasks

    private var notificationId: Int = 0

    // Add task in Room
    fun addTask(todoItem: TodoItem){
        repository.addTask(todoItem)
//        increaseCompletedTasks(1)
    }

    // Add task in Room with creating Notification Alarm
    fun addTaskWithAlarm(
        context: Context,
        todoItem: TodoItem,
        triggerAtMillis: Long
    ){
        repository.addTaskWithAlarm(context, todoItem, triggerAtMillis)
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
        updateTaskInAdapter(todoItem)
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

    fun setCurrModel(todoItem: TodoItem) = repository.setCurrModel(todoItem)

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

    // Setup deadline notification
    fun setNotificationAlarm(context: Context, todoItem: TodoItem, triggerAtMillis: Long){
        repository.setNotificationAlarm(context, todoItem, triggerAtMillis)
    }

    // Cancel deadline notification
    fun cancelNotificationAlarm(context: Context, todoItem_id: Int){
        repository.cancelNotificationAlarm(context, todoItem_id)
    }
}