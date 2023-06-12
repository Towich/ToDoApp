package com.example.todoapp.ui.Repository

import androidx.lifecycle.MutableLiveData
import com.example.todoapp.data.model.TodoItem

class StartRepository {

    private val works: MutableLiveData<List<TodoItem>> = MutableLiveData<List<TodoItem>>()
    private var currentId: String = "0"

    init {
        works.value = ArrayList<TodoItem>()
        addWork(TodoItem("НАДО ЧТО-ТО КУПИТЬ!!", "срочная"))
        addWork(TodoItem("ТОЧНО НАДО ЧТО-ТО КУПИТЬ", "обычная"))
        addWork(TodoItem( "надо что-то реально купить..", "низкая"))
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