package com.example.todoapp.ui.Repository

import androidx.lifecycle.MutableLiveData
import com.example.todoapp.data.model.TodoItem

class StartRepository {

    private val works: MutableLiveData<List<TodoItem>> = MutableLiveData<List<TodoItem>>()
    private var currentId: String = "0"

    init {
        works.value = ArrayList()
        addWork(TodoItem("НАДО ЧТО-ТО КУПИТЬ!!", "Высокий"))
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

    fun addWork(work: TodoItem){
        work.id = currentId
        currentId = (currentId.toInt() + 1).toString()
        works.value = works.value?.plus(work)
    }
}