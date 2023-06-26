package com.example.todoapp.data.model

import com.example.todoapp.data.db.TaskEntity

data class TodoItem(var textCase: String, var importance: String, var deadlineData: String = ""){
    var id: String = "0"
    var completed: Boolean = false

    fun toEntity(): TaskEntity {
        return TaskEntity(id.toInt(), textCase, importance, deadlineData, completed)
    }
}
