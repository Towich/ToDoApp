package com.example.todoapp.data.model

import com.example.todoapp.data.database.TaskEntity

data class TodoItem(
    var id: Int,
    var textCase: String,
    var importance: String,
    var deadlineData: String,
    var completed: Boolean
){
    fun toEntity(): TaskEntity = TaskEntity(null ,textCase, importance, deadlineData, completed)
}
