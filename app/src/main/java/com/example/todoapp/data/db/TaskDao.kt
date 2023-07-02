package com.example.todoapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM task WHERE completed = 0")
    fun getAllUncompletedTasks(): List<TaskEntity>

    @Insert(entity = TaskEntity::class, onConflict = OnConflictStrategy.NONE)
    fun insertTask(taskEntity: TaskEntity)

    @Delete
    fun deleteTask(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE id = :id")
    fun deleteTaskById(id: Int)

    @Query("UPDATE task SET " +
            "textCase = :textCase, " +
            "importance = :importance, " +
            "deadlineData = :deadlineData, " +
            "completed = :completed " +
            "WHERE id = :id")
    fun updateEntity(id: Int,
        textCase: String,
        importance: String,
        deadlineData: String,
        completed: Boolean
    )

}

