package com.example.todoapp.ui.Adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem

class CustomRecyclerAdapter(private var tasks: List<TodoItem>)
    : RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    private var onClickListenerCheckBoxButton: OnClickListener? = null
    private var onClickListenerInfoButton: OnClickListener? = null

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
        return tasks.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val todoItem: TodoItem = tasks.get(position) ?: return

        holder.checkBox.text = todoItem.textCase
        holder.checkBox.isChecked = todoItem.completed

        val importance: Boolean = todoItem.importance == "Высокий"

        updateStatusOfWork(holder.checkBox, todoItem.completed, importance)

        // Click on CheckBox
        holder.checkBox.setOnClickListener {
            run {
                updateStatusOfWork(holder.checkBox, !todoItem.completed, importance)
                todoItem.completed = !todoItem.completed

                if(onClickListenerCheckBoxButton != null){
                    onClickListenerCheckBoxButton!!.onClick(todoItem)
                }
            }
        }

        // Click on "Button Info"
        holder.buttonInfo.setOnClickListener {
            if (onClickListenerInfoButton != null) {
                onClickListenerInfoButton!!.onClick(todoItem)
            }
        }
    }

    // Changing CheckBox by clicking on it
    private fun updateStatusOfWork(compoundButton: CompoundButton, newStatus: Boolean, isHighImportance: Boolean){

        // If CHECKED
        if(newStatus){

            // Set "STRIKE_THRU" for CheckBox's Text
            compoundButton.paintFlags =
                compoundButton.paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)

            // Set GRAY color for CheckBox's Text
            compoundButton.setTextColor(
                ContextCompat.getColor(
                    compoundButton.context,
                    R.color.gray_33
                )
            )

            compoundButton.setButtonDrawable(R.drawable.checked)
        } // If NOT CHECKED
        else {

            // Remove "STRIKE_THRU" from CheckBox's Text
            compoundButton.paintFlags =
                compoundButton.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            // Set WHITE color for CheckBox's Text
            compoundButton.setTextColor(
                ContextCompat.getColor(
                    compoundButton.context,
                    R.color.white
                )
            )

            // If TodoItem has "Высокий" importance
            if(isHighImportance)
                compoundButton.setButtonDrawable(R.drawable.unchecked__1_)  // red square
            else
                compoundButton.setButtonDrawable(R.drawable.unchecked)      // gray square
        }
    }

    // A functions to bind the onclickListeners.
    fun setOnClickListenerInfoButton(onClickListener: OnClickListener) {
        this.onClickListenerInfoButton = onClickListener
    }
    fun setOnClickListenerCheckBoxButton(onClickListener: OnClickListener) {
        this.onClickListenerCheckBoxButton = onClickListener
    }

    fun getTasks(): List<TodoItem> = tasks
    fun setTasks(tasks: List<TodoItem>){
        this.tasks = tasks
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(model: TodoItem)
    }

}

