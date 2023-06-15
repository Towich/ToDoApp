package com.example.todoapp.ui.Repository

import androidx.lifecycle.MutableLiveData
import com.example.todoapp.data.model.TodoItem
import java.util.Calendar

class StartRepository {

    private val works: MutableLiveData<List<TodoItem>> = MutableLiveData<List<TodoItem>>()
    private var currentId: String = "0"

    val MONTHS = arrayOf("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря")
    init {
        works.value = mutableListOf(TodoItem("НАДО ЧТО-ТО КУПИТЬ!!", "Высокий"))
        addWork(TodoItem("ТОЧНО НАДО ЧТО-ТО КУПИТЬ", "Высокий"))
        addWork(TodoItem( "три", "низкая"))
        addWork(TodoItem( "четары.", "Высокий"))
        addWork(TodoItem( "петь", "Высокий"))
        addWork(TodoItem( "шышсть", "низкая"))
        addWork(TodoItem( "севен", "низкая"))
        addWork(TodoItem( "уосемь", "Высокий"))
        addWork(TodoItem( "деват", "низкая"))
        addWork(TodoItem( "десат", "Высокий"))
    }


    fun getWorks(): MutableLiveData<List<TodoItem>> {
        return works
    }

    fun getWorkById(id: String): TodoItem? {
        return works.value?.find { it.id == id }
    }
    fun addWork(work: TodoItem){
        val newList = works.value!!.toMutableList()

        work.id = currentId
        currentId = (currentId.toInt() + 1).toString()

        newList.add(0, work)

        works.value = newList
    }

    fun removeWork(todoItem: TodoItem){
        works.value = works.value?.minus(todoItem)
    }

    fun getCurrentDate(): List<Int> {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return listOf(year, month, day)
    }
}