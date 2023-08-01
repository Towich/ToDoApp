package com.example.todoapp.data.Repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.data.database.TaskDao
import com.example.todoapp.data.database.TaskEntity
import com.example.todoapp.data.di.ApplicationScope
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.network.ApiService
import com.example.todoapp.data.network.FileReader
import com.example.todoapp.data.network.RequestCallback
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import com.example.todoapp.ui.Adapter.UsersDiffCallback
import com.example.todoapp.ui.Service.MyReceiver
import com.example.todoapp.ui.util.NotificAlarmManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ApplicationScope
class StartRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val apiService: ApiService,
    private val fileReader: FileReader
) {

    private var tasks: List<TodoItem> = listOf()
    private var currModel: TodoItem? = null
    private var isCurrEditing: Boolean = false // Is current editing or creating a new work
    private val mAdapter = CustomRecyclerAdapter(mutableListOf())
    private var showingSnackbar: Snackbar? = null
    var completedTasks: MutableLiveData<Int> = MutableLiveData()
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
    val CHANNEL_ID = "77"

    // Add tasks to Room
    fun addTask(todoItem: TodoItem) = runBlocking {
        launch {
            withContext(Dispatchers.IO) {
                taskDao.insertTask(todoItem.toEntity())
                getTasks()
            }
        }

        Handler(Looper.getMainLooper()).post {
            getAdapter().notifyItemInserted(mAdapter.getTasks().size)
        }
    }

    fun addTaskWithAlarm(
        context: Context,
        todoItem: TodoItem,
        triggerAtMillis: Long
    ) = runBlocking {

        val newList = mutableListOf<TodoItem>()

        launch {
            withContext(Dispatchers.IO) {
                taskDao.insertTask(todoItem.toEntity())

                val dbList = taskDao.getAllTasks()
                for (e in dbList) {
                    newList.add(e.toModel())
                }

                setNotificationAlarm(context, newList[newList.size-1], triggerAtMillis)
            }
        }

        Handler(Looper.getMainLooper()).post {
            getAdapter().notifyItemInserted(mAdapter.getTasks().size)
        }
    }

    // Get all tasks from Room
    suspend fun getTasks(): List<TodoItem> {
        return transformTasks(false)
    }

    // Get all not completed tasks from Room
    suspend fun getAllUncompletedTasks(): List<TodoItem> {
        return transformTasks(true)
    }

    // Get all completed tasks from Room
    suspend fun getAllCompletedTasks(): List<TodoItem> = withContext(Dispatchers.IO) {
        val newList = mutableListOf<TodoItem>()

        val dbList = taskDao.getAllCompletedTasks()
        for (e in dbList) {
            newList.add(e.toModel())
        }

        return@withContext newList
    }

    // Get tasks from Room and convert them from Entity to Model
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


    // Remove tasks from Room
    suspend fun removeTask(todoItem: TodoItem) {
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

    // Set new list of tasks in Adapter
    fun setAllTasks(brandNewList: List<TodoItem>) {

        val diffCallback = UsersDiffCallback(mAdapter.getTasks(), brandNewList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(brandNewList)
        diffResult.dispatchUpdatesTo(mAdapter)
    }

    // Current model
    fun setCurrModel(newModel: TodoItem) {
        currModel = newModel
    }

    fun clearCurrModel() {
        currModel = null
    }

    fun getCurrModel() = currModel

    // isCurrentEditing

    fun setCurrEditing(newState: Boolean) {
        isCurrEditing = newState
    }

    fun isCurrEditing() = isCurrEditing
    fun clearCurrEditing() {
        isCurrEditing = false
    }

    // This function is used for HTTPS methods @GET and @POST
    // with mock responses by MockWebServer.

    // Doesn't participate in project.
    suspend fun testMockWebServer(callback: RequestCallback) {
        withContext(Dispatchers.IO) {
            val mockWebServer = MockWebServer()
            mockWebServer.enqueue(
                MockResponse()
                    .setBody(fileReader.readStringFromFile("success_response_1.json"))
                    .setResponseCode(200)
                    .setBodyDelay(2, TimeUnit.SECONDS)
            )
            mockWebServer.start()

            val myUrl = mockWebServer.url("/v1/")

            apiService.getRequest(myUrl.toString(), object : RequestCallback {
                override fun onSuccess(response: String) {
                    val todoItem = Gson().fromJson(response, TodoItem::class.java)
                    addTask(todoItem)

                    callback.onSuccess("success")
                }

                override fun onFailure(error: String) {
                    // TODO: implement this
                    callback.onFailure("error")
                }
            })
        }
    }

    fun getChannelID(): String {
        return CHANNEL_ID
    }

    // Create Notification alarm at specific time
    fun setNotificationAlarm(context: Context, todoItem: TodoItem, triggerAtMillis: Long) {
        NotificAlarmManager.setNotificationAlarm(context, todoItem, triggerAtMillis)
    }

    // Cancel Notification alarm
    fun cancelNotificationAlarm(context: Context, todoItem_id: Int) {
        NotificAlarmManager.cancelNotificationAlarm(context, todoItem_id)
    }

    // Show Snackbar with option of cancel removing task
    fun showSnackbarCancelRemove(
        context: Context,
        mView: View,
        deleteItem: TodoItem
    ){
        val snackBarText = "Удалено: " + deleteItem.textCase    // Snackbar's Text
        val snackBarDuration = Snackbar.LENGTH_LONG             // Snackbar's Duration
        val snackBarTextButton = "ОТМЕНИТЬ"

        // Instantiate Snackbar
        val snackbar = Snackbar.make(
            mView,
            snackBarText,
            snackBarDuration
        )

        // Set action for Snackbar's button "Cancel"
        snackbar.setAction(snackBarTextButton) {

            // Add task to Room
            addTask(deleteItem)

            // Refresh list in Adapter of RecyclerView
            runBlocking {
                val tasksList = getTasks()
                setAllTasks(tasksList)
            }

            // If deleted task has been completed,
            // increase counter of completed tasks
            if (deleteItem.completed)
                completedTasks.value = completedTasks.value?.plus(1)
        }

        // Add callback to Snackbar
        snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            // Callback of Snackbar's appear
            override fun onShown(transientBottomBar: Snackbar?) {
                super.onShown(transientBottomBar)
            }

            // Callback of Snackbar's dismiss
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)

                if (event != DISMISS_EVENT_ACTION) {    // if Snackbar dismissed by any case, otherwise by action
                    // Cancel Notification
                    cancelNotificationAlarm(
                        context,
                        deleteItem.id
                    )
                }
            }
        })

        snackbar.show()
        showingSnackbar = snackbar
    }

    fun dismissShowingSnackbar(){
        if(showingSnackbar != null){
            showingSnackbar?.dismiss()
            showingSnackbar = null
        }
    }
}