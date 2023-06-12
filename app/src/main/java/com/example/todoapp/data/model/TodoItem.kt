package com.example.todoapp.data.model

data class TodoItem(var textCase: String, var importance: String){
    var id: String = "0"
    var completed: Boolean = false
}
