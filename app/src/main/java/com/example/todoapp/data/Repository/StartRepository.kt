package com.example.todoapp.data.Repository

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.data.database.TaskDao
import com.example.todoapp.data.database.TaskEntity
import com.example.todoapp.data.di.ApplicationScope
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import com.example.todoapp.ui.Adapter.UsersDiffCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@ApplicationScope
class StartRepository @Inject constructor(private val taskDao: TaskDao) {

    private var tasks: List<TodoItem> = listOf()
    private var currModel: TodoItem? = null
    private var isCurrEditing: Boolean? = null // Is current editing or creating a new work

    val months = arrayOf(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря"
    )

    private val mAdapter = CustomRecyclerAdapter(mutableListOf())
    var completedTasks: MutableLiveData<Int> = MutableLiveData()

    fun addTask(todoItem: TodoItem) = runBlocking {
        launch {
            withContext(Dispatchers.IO) {
                taskDao.insertTask(todoItem.toEntity())
                getTasks()
            }

//            val diffCallback = UsersDiffCallback(tasks, newList)
//            val diffResult = DiffUtil.calculateDiff(diffCallback)

//            mAdapter.setTasks(newList)
//            diffResult.dispatchUpdatesTo(mAdapter)
//            mAdapter.notifyItemInserted(tasks.size)
        }

    }

    suspend fun getTasks(): List<TodoItem> {
        tasks = transformTasks(false)
        return tasks
    }

    suspend fun getAllUncompletedTasks(): List<TodoItem> {
        return transformTasks(true)
    }

    suspend fun getAllCompletedTasks(): List<TodoItem> = withContext(Dispatchers.IO) {
        val newList = mutableListOf<TodoItem>()

        val dbList = taskDao.getAllCompletedTasks()
        for (e in dbList) {
            newList.add(e.toModel())
        }

        return@withContext newList
    }

    private suspend fun transformTasks(isGetUncompletedTasks: Boolean): List<TodoItem> =
        withContext(Dispatchers.IO) {
            val newList = mutableListOf<TodoItem>()

            val dbList: List<TaskEntity> = if (isGetUncompletedTasks)
                taskDao.getAllUncompletedTasks()
            else {
                taskDao.getAllTasks()
            }

            for (e in dbList) {
                newList.add(e.toModel())
            }
            return@withContext newList
        }


    suspend fun removeWork(todoItem: TodoItem) {
        val oldWorks = mAdapter.getTasks()
        val newWorks = mAdapter.getTasks().minus(todoItem)

        withContext(Dispatchers.IO) {
            taskDao.deleteTaskById(todoItem.id)
        }

        val diffCallback = UsersDiffCallback(oldWorks, newWorks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        tasks = newWorks
        mAdapter.setTasks(newWorks)
        diffResult.dispatchUpdatesTo(mAdapter)
    }

    suspend fun updateTask(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            taskDao.updateEntity(
                todoItem.id,
                todoItem.textCase,
                todoItem.importance,
                todoItem.deadlineData,
                todoItem.completed
            )
        }
    }

    fun updateTaskInAdapter(todoItem: TodoItem) {
        val newList = mutableListOf<TodoItem>()

        for (index in mAdapter.getTasks().indices) {
            if (mAdapter.getTasks()[index].id == todoItem.id) {
                newList.add(todoItem)
            } else {
                newList.add(mAdapter.getTasks()[index])
            }
        }

        tasks = newList
        mAdapter.setTasks(newList)
    }

    fun getCurrentDate(): List<Int> {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return listOf(year, month, day)
    }

    fun getAdapter(): CustomRecyclerAdapter = mAdapter

    fun setAllTasks(brandNewList: List<TodoItem>) {

        val diffCallback = UsersDiffCallback(mAdapter.getTasks(), brandNewList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(brandNewList)
        diffResult.dispatchUpdatesTo(mAdapter)
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
}