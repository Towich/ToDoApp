package com.example.todoapp.ui.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.data.model.TodoItem

class UsersDiffCallback(
    private val oldList: List<TodoItem>,
    private val newList: List<TodoItem>
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTodoItem = oldList[oldItemPosition]
        val newTodoItem = newList[newItemPosition]

        return oldTodoItem.id == newTodoItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTodoItem = oldList[oldItemPosition]
        val newTodoItem = newList[newItemPosition]

        return oldTodoItem == newTodoItem
    }
}