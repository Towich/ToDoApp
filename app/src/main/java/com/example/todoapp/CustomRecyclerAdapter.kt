package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerAdapter(private val works: List<TodoItem>) :
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val checkBox : CheckBox = itemView.findViewById(R.id.checkbox)
        val buttonInfo : ImageButton = itemView.findViewById(R.id.button_info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.work_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return works.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.checkBox.text = works[position].textCase
    }
}