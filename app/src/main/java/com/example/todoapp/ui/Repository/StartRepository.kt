package com.example.todoapp.ui.Repository

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.data.db.TaskDao
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import com.example.todoapp.ui.Adapter.UsersDiffCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar

class StartRepository(
    private val taskDao: TaskDao
    )
{

    private var tasks: List<TodoItem> = listOf()

    val MONTHS = arrayOf(
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

    init {

    }

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
        val listTodoItem = transformTasks()
        tasks = listTodoItem
        mAdapter.setTasks(listTodoItem)
        return listTodoItem
    }

    suspend fun transformTasks(): List<TodoItem> = withContext(Dispatchers.IO) {
        val newList = mutableListOf<TodoItem>()
        val dbList = taskDao.getAllTasks()

        for (e in dbList) {
            newList.add(e.toModel())
        }
        return@withContext newList
    }

    suspend fun removeWork(todoItem: TodoItem) {
        val oldWorks = mAdapter.getTasks()
        val newWorks = mAdapter.getTasks().minus(todoItem)

        withContext(Dispatchers.IO){
            taskDao.deleteTaskById(todoItem.id)
        }

        val diffCallback = UsersDiffCallback(oldWorks, newWorks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(newWorks)
        diffResult.dispatchUpdatesTo(mAdapter)
//        mAdapter.notifyItemRemoved(position)
    }

    fun getCurrentDate(): List<Int> {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return listOf(year, month, day)
    }

    fun getAdapter(): CustomRecyclerAdapter = mAdapter

    fun getTask(index: Int) = mAdapter.getTasks()[index]


    fun setAllTasks(brandNewList: List<TodoItem>) {

        val diffCallback = UsersDiffCallback(mAdapter.getTasks(), brandNewList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(brandNewList)
        diffResult.dispatchUpdatesTo(mAdapter)
    }
}