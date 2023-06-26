package com.example.todoapp.ui.Repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.data.Dependencies
import com.example.todoapp.data.db.TaskDao
import com.example.todoapp.data.db.TaskEntity
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import com.example.todoapp.ui.Adapter.UsersDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar

class StartRepository(private val taskDao: TaskDao) {

    private var tasks: MutableList<TodoItem> = mutableListOf()
    private lateinit var newList: MutableList<TodoItem>
    private var removedCompletedItems: MutableList<TodoItem> = mutableListOf()

    private val works: MutableLiveData<List<TodoItem>> = MutableLiveData<List<TodoItem>>()
    private val uncompletedTasks: MutableLiveData<List<TodoItem>> = MutableLiveData<List<TodoItem>>()
    private var currentId: String = "0"

    val MONTHS = arrayOf("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря")

    private val mAdapter = CustomRecyclerAdapter(mutableListOf())
    init {
        works.value = mutableListOf()
        uncompletedTasks.value = mutableListOf()

//        addWork(TodoItem("НАДО ЧТО-ТО КУПИТЬ!!", "Высокий"))
//        addWork(TodoItem("ТОЧНО НАДО ЧТО-ТО КУПИТЬ", "Высокий"))
//        addWork(TodoItem( "три", "низкая"))
//        addWork(TodoItem( "четары.", "Высокий"))
//        addWork(TodoItem( "петь", "Высокий"))
//        addWork(TodoItem( "шышсть", "низкая"))
//        addWork(TodoItem( "севен", "низкая"))
//        addWork(TodoItem( "уосемь", "Высокий"))
//        addWork(TodoItem( "деват", "низкая"))
//        addWork(TodoItem( "десат", "Высокий"))
//        addWork(TodoItem( "купить хлеб", "низкая"))
//        addWork(TodoItem( "купить молоко.", "Высокий"))
//        addWork(TodoItem( "квас", "Высокий"))
//        addWork(TodoItem( "картошка", "низкая"))
//        addWork(TodoItem( "лук", "низкая"))
//        addWork(TodoItem( "морковь", "Высокий"))
//        addWork(TodoItem( "приправа для плова", "низкая"))
//        addWork(TodoItem( "рис", "Высокий"))

        val list = mutableListOf<TodoItem>()
        list.add(TodoItem("НАДО ЧТО-ТО КУПИТЬ!!", "Высокий"))
        list.add(TodoItem("ТОЧНО НАДО ЧТО-ТО КУПИТЬ", "Высокий"))
        list.add(TodoItem( "трhfghи", "низкая"))
        list.add(TodoItem( "чеgfhтары.", "Высокий"))
        list.add(TodoItem("НАfghДО ЧТО-ТО КУПИТЬ!!", "Высокий"))
        list.add(TodoItem("ТОЧНfghО НАДО ЧТО-ТО КУПИТЬ", "Высокий"))
        list.add(TodoItem( "тfghdfgри", "низкая"))
        list.add(TodoItem( "четаdfgdfры.", "Высокий"))
        list.add(TodoItem("НАДОgdfgd ЧТО-ТО КУПИТЬ!!", "Высокий"))
        list.add(TodoItem("ТОЧНОgdfg НАДО ЧТО-ТО КУПhfgИТЬ", "Высокий"))
        list.add(TodoItem( "hfghтри", "низhкая"))
        list.add(TodoItem( "четаdsfgdfры.", "Высокий"))
        list.add(TodoItem("НАsdfsdДО ЧТО-ТО КУПИТЬ!!", "Высокий"))
        list.add(TodoItem("ТОЧНО НАhfghfgО ЧТО-ТО КУПИТЬ", "Высокий"))
        list.add(TodoItem( "трsdfsdи", "низкая"))
        list.add(TodoItem( "четарhgfhfgы.", "Высокий"))

        val diffCallback = UsersDiffCallback(mutableListOf(), mAdapter.getTasks())
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(list)
        diffResult.dispatchUpdatesTo(mAdapter)

        //addTask()
    }

    fun addTask(taskEntity: TaskEntity) = runBlocking{
        launch {
            withContext(Dispatchers.IO){
                //taskDao.insertTask(TaskEntity(1, "мойтекст", "Важный", "27 июня", true))
                taskDao.insertTask(taskEntity)
            }

        }
    }

    fun getWorks(): MutableLiveData<List<TodoItem>> {
        return works
    }

    fun getUncompletedTasks(): MutableLiveData<List<TodoItem>>{
        return uncompletedTasks
    }

    fun getSizeCompletedTasks(): Int = removedCompletedItems.size

    fun addWork(work: TodoItem){
        val oldList = mAdapter.getTasks()
        val newList = oldList

        work.id = currentId
        currentId = (currentId.toInt() + 1).toString()

        newList.add(work)
        addTask(work.toEntity())

        val diffCallback = UsersDiffCallback(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(newList)
        diffResult.dispatchUpdatesTo(mAdapter)
        mAdapter.notifyItemInserted(oldList.size)
    }

    fun addUncompletedTask(task: TodoItem){
        val newListUncompleted = uncompletedTasks.value!!.toMutableList()
        newListUncompleted.add(0, task)
        uncompletedTasks.value = newListUncompleted
    }

    fun removeUncompletedTask(task: TodoItem){
        uncompletedTasks.value = uncompletedTasks.value?.minus(task)
    }

    fun removeWork(position: Int){
        val oldWorks = mAdapter.getTasks()
        val newWorks = oldWorks
        newWorks.removeAt(position)

        val diffCallback = UsersDiffCallback(oldWorks, newWorks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(newWorks)
        diffResult.dispatchUpdatesTo(mAdapter)
        mAdapter.notifyItemRemoved(position)
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

    fun setUncompletedTasks(){
        val oldList = mAdapter.getTasks()
        newList = mutableListOf()
        removedCompletedItems = mutableListOf()

        var i = 0
        var removedItems = 0
        for(item in oldList){
            if(!item.completed){
                newList.add(item)
            }
            else{
                removedCompletedItems.add(item)
                mAdapter.notifyItemRemoved(i - removedItems)
                removedItems++
            }
            i++
        }

        mAdapter.setTasks(newList)
    }


    fun setAllTasks(){
        val brandNewList = mutableListOf<TodoItem>()

        for(item in mAdapter.getTasks()){
            brandNewList.add(item)
        }

        for (item in removedCompletedItems){
            brandNewList.add(item)
            mAdapter.notifyItemInserted(mAdapter.getTasks().size)
        }

//        val diffCallback = UsersDiffCallback(mAdapter.getTasks(), tasks)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(brandNewList)
//        diffResult.dispatchUpdatesTo(mAdapter)
    }
}