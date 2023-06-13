package com.example.todoapp.ui.Adapter

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem

class CustomRecyclerAdapter(private val works: List<TodoItem>) :
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

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

        val todoItem: TodoItem = works[position]

        holder.checkBox.text = todoItem.textCase
        holder.checkBox.isChecked = todoItem.completed

        val importance: Boolean = todoItem.importance == "Высокий"

        updateStatusOfWork(holder.checkBox, todoItem.completed, importance)

        holder.checkBox.setOnClickListener {
            run {

                updateStatusOfWork(holder.checkBox, !todoItem.completed, importance)

                todoItem.completed = !todoItem.completed
            }
        }

//            Toast.makeText(compoundButton.context, "currentId: " + works[position].id, Toast.LENGTH_SHORT).show()

        holder.buttonInfo.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(todoItem)
            }
        }
    }
    private fun updateStatusOfWork(compoundButton: CompoundButton, newStatus: Boolean, isHighImportance: Boolean){
        if(newStatus){
            compoundButton.paintFlags =
                compoundButton.paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)

            compoundButton.setTextColor(
                ContextCompat.getColor(
                    compoundButton.context,
                    R.color.gray_33
                )
            )

            compoundButton.setButtonDrawable(R.drawable.checked)
        }
        else {
            compoundButton.paintFlags =
                compoundButton.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            compoundButton.setTextColor(
                ContextCompat.getColor(
                    compoundButton.context,
                    R.color.white
                )
            )

            if(isHighImportance)
                compoundButton.setButtonDrawable(R.drawable.unchecked__1_)
            else
                compoundButton.setButtonDrawable(R.drawable.unchecked)
        }
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(model: TodoItem)
    }

}