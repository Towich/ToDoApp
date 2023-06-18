package com.example.todoapp.ui.Repository

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import com.example.todoapp.ui.Adapter.UsersDiffCallback
import java.util.Calendar

class StartRepository {


    private var tasks: MutableList<TodoItem> = mutableListOf()
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

        var list = mutableListOf<TodoItem>()
        list.add(TodoItem("НАДО ЧТО-ТО КУПИТЬ!!", "Высокий"))
        list.add(TodoItem("ТОЧНО НАДО ЧТО-ТО КУПИТЬ", "Высокий"))
        list.add(TodoItem( "три", "низкая"))
        list.add(TodoItem( "четары.", "Высокий"))
        list.add(TodoItem("НАДО ЧТО-ТО КУПИТЬ!!", "Высокий"))
        list.add(TodoItem("ТОЧНО НАДО ЧТО-ТО КУПИТЬ", "Высокий"))
        list.add(TodoItem( "три", "низкая"))
        list.add(TodoItem( "четары.", "Высокий"))
        list.add(TodoItem("НАДО ЧТО-ТО КУПИТЬ!!", "Высокий"))
        list.add(TodoItem("ТОЧНО НАДО ЧТО-ТО КУПИТЬ", "Высокий"))
        list.add(TodoItem( "три", "низкая"))
        list.add(TodoItem( "четары.", "Высокий"))
        list.add(TodoItem("НАДО ЧТО-ТО КУПИТЬ!!", "Высокий"))
        list.add(TodoItem("ТОЧНО НАДО ЧТО-ТО КУПИТЬ", "Высокий"))
        list.add(TodoItem( "три", "низкая"))
        list.add(TodoItem( "четары.", "Высокий"))

        val diffCallback = UsersDiffCallback(mAdapter.getTasks(), list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(list)
        diffResult.dispatchUpdatesTo(mAdapter)
    }


    fun getWorks(): MutableLiveData<List<TodoItem>> {
        return works
    }

    fun getUncompletedTasks(): MutableLiveData<List<TodoItem>>{
        return uncompletedTasks
    }

    fun getSizeCompletedTasks(): Int = works.value!!.size - uncompletedTasks.value!!.size

    fun addWork(work: TodoItem){
        val oldList = mAdapter.getTasks()
        val newList = oldList

        work.id = currentId
        currentId = (currentId.toInt() + 1).toString()

        newList.add(work)

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
        tasks = mAdapter.getTasks()
        val oldList = mAdapter.getTasks()
        val newList = mutableListOf<TodoItem>()

        var i: Int = 0
        for (item in oldList){
            if (!item.completed) {
                newList.add(item)
            }
            else{
                mAdapter.notifyItemRemoved(i)
            }
            i++
        }

        val diffCallback = UsersDiffCallback(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(newList)
        diffResult.dispatchUpdatesTo(mAdapter)
        //mAdapter.notifyDataSetChanged()
    }

    fun setAllTasks(){
        val oldList = tasks
        val newList = mAdapter.getTasks()

        var i: Int = 0
        for (item in oldList){
            if (item.completed) {
                mAdapter.notifyItemInserted(i)
                newList.add(i, item)
            }
            i++
        }

        val diffCallback = UsersDiffCallback(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mAdapter.setTasks(newList)
        diffResult.dispatchUpdatesTo(mAdapter)
    }
}