package com.example.todoapp

import androidx.lifecycle.MutableLiveData

class MainRepository {

    private val works: MutableLiveData<List<TodoItem>> = MutableLiveData<List<TodoItem>>()

    init {
        works.value = listOf(
            TodoItem("1", "НАДО ЧТО-ТО КУПИТЬ!!", "срочная"),
            TodoItem("2", "ТОЧНО НАДО ЧТО-ТО КУПИТЬ", "обычная"),
            TodoItem("3", "надо что-то реально купить..", "низкая")
        )
    }

    public fun getWorks(): MutableLiveData<List<TodoItem>> {
        return works
    }
}