package com.example.todoapp.ui.Adapter

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem

class CustomRecyclerAdapter(private val works: List<TodoItem>) :
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        val buttonInfo: ImageButton = itemView.findViewById(R.id.button_info)
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
        holder.checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
            run {
                if (isChecked) {
                    compoundButton.paintFlags =
                        compoundButton.paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)

                    compoundButton.setTextColor(
                        ContextCompat.getColor(
                            holder.checkBox.context,
                            R.color.gray_33
                        )
                    )
                } else {
                    compoundButton.paintFlags =
                        compoundButton.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

                    compoundButton.setTextColor(
                        ContextCompat.getColor(
                            holder.checkBox.context,
                            R.color.white
                        )
                    )
                }
            }
            Toast.makeText(compoundButton.context, "currentId: " + works[position].id, Toast.LENGTH_SHORT).show()
        }
    }
}