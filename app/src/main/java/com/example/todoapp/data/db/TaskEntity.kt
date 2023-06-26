package com.example.todoapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "textCase") val textCase: String?,
    @ColumnInfo(name = "importance") val importance: String?,
    @ColumnInfo(name = "deadlineData") val deadlineData: String?,
    @ColumnInfo(name = "completed") val completed: Boolean?
)
