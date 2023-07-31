package com.example.todoapp.ui.ViewModel

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.*
import com.example.todoapp.data.Repository.StartRepository
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.network.RequestCallback
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartViewModel @Inject constructor(
    var repository: StartRepository,
    var applicationContext: Context
) : ViewModel() {

    var completedTasks: MutableLiveData<Int> = repository.completedTasks
    var showingUncompletedTasks: Boolean = false

    // Tasks

    // Get all tasks from Room
    // and setup into RecyclerView's Adapter
    fun setupTasks() {
        viewModelScope.launch {
            val tasksList = repository.getTasks()
//            repository.getAdapter().notifyDataSetChanged()
            repository.setAllTasks(tasksList)
        }
    }

    // Get all uncompleted tasks from Room
    // and setup into RecyclerView's Adapter
    fun setupUncompletedTasks(){
        viewModelScope.launch {
            val tasksList = repository.getAllUncompletedTasks()
            repository.setAllTasks(tasksList)
        }
    }

    // Get count of completed tasks
    fun getQuantityOfCompletedTasks(){
        viewModelScope.launch {
            val tasksList = repository.getAllCompletedTasks()
            completedTasks.value = tasksList.size
        }
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

    // Set current model for editing in EditWorkFragment
    fun setCurrModel(newModel: TodoItem){
        repository.setCurrModel(newModel)
    }

    // Set current editing flag for editing in EditWorkFragment
    fun setCurrEditing(newState: Boolean){
        repository.setCurrEditing(newState)
    }
    fun getAdapter(): CustomRecyclerAdapter = repository.getAdapter()

    // Increase/decrease counter of completed tasks
    fun increaseCompletedTasks(delta: Int){
        completedTasks.value = completedTasks.value?.plus(delta)
    }

    // Test MockWebServer
    fun testMockWebServer(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.testMockWebServer(object: RequestCallback {
                override fun onSuccess(response: String) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(applicationContext, "SUCCESS", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(error: String) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    // Cancel deadline notification
    fun cancelNotificationAlarm(context: Context, todoItem_id: Int){
        repository.cancelNotificationAlarm(context, todoItem_id)
    }
}