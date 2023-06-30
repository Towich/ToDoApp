package com.example.todoapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.data.model.TodoItem

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "textCase") val textCase: String,
    @ColumnInfo(name = "importance") val importance: String,
    @ColumnInfo(name = "deadlineData") val deadlineData: String,
    @ColumnInfo(name = "completed") val completed: Boolean
){
    fun toModel() = TodoItem(id!!, textCase, importance, deadlineData, completed)
}
