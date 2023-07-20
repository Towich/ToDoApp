package com.example.todoapp.ui.Fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.databinding.FragmentEditWorkBinding
import com.example.todoapp.ui.ViewModel.EditTaskViewModel
import com.example.todoapp.ui.ViewModel.StartViewModel
import javax.inject.Inject

/**
 * A fragment which shows creating new or editing existing task.
 */
class EditWorkFragment : Fragment() {


    private var _binding: FragmentEditWorkBinding? = null

    // ViewModel Factory
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // ViewModel
    private lateinit var viewModel: EditTaskViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditWorkBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inject
        (requireContext().applicationContext as App).appComponent.startComponent().create().inject(this)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditTaskViewModel::class.java)

        // onClick on "Close"
        binding.imagebuttonClose.setOnClickListener {
            mPopBackStack()
        }

        // onClick on choosing "Importance"
        val buttonShowImportancePopupMenu = binding.textImportanceTitle
        binding.textImportanceTitle.setOnClickListener {
            showImportancePopupMenu(buttonShowImportancePopupMenu)
        }

        // onClick on "Deadline"
        binding.buttonSetDeadline.isEnabled = false
        binding.buttonSetDeadline.setOnClickListener {
            showDateDialog()
        }

        // onClick on switcher of "Deadline"
        binding.switcherDeadline.setOnClickListener {
            if(binding.switcherDeadline.isChecked){
                binding.buttonSetDeadline.isEnabled = true
                showDateDialog()
            }
            else{
                binding.buttonSetDeadline.isEnabled = false
                binding.textViewSelectedDeadline.text = ""
            }
        }

        connectButtonSave()
        connectButtonDelete()
        loadEditingModel()
    }

    // Show Popup menu with choosing importance
    private fun showImportancePopupMenu(v: View){

        // Create an instance of PopupMenu
        val popupMenu = PopupMenu(context, v)
        popupMenu.menuInflater.inflate(R.menu.menu_importance, popupMenu.menu)

        // Change color of importance "High" to RED
        val highImportanceItem = popupMenu.menu.getItem(2)
        val spannable = SpannableString(highImportanceItem.title.toString())
        spannable.setSpan(ForegroundColorSpan(
            ContextCompat.getColor(v.context, R.color.red)),
            0,
            spannable.length,
            0
        )
        highImportanceItem.title = spannable

        // onClick on menu item "Importance"
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem: MenuItem? ->
            binding.textImportanceBody.text = menuItem?.title

            // If importance = "High" --> change color to Red
            // else --> to Gray
            if(menuItem!!.itemId == R.id.menu_importance_high)
                binding.textImportanceBody.setTextColor(ContextCompat.getColor(v.context, R.color.red))
            else
                binding.textImportanceBody.setTextColor(ContextCompat.getColor(v.context, R.color.gray_66))
            true
        })

        popupMenu.show()
    }

    // Set text in EditText and chosen importance by current TodoItem
    private fun loadEditingModel(){
        val todoItem = viewModel.getCurrModel() ?: return

        // Load Text of task
        binding.editText.setText(todoItem.textCase)

        // Load importance
        var importStr = todoItem.importance
        if(importStr == "Высокий") {
            importStr = "!!$importStr"
            binding.textImportanceBody.setTextColor(
                ContextCompat.getColor(
                    binding.editText.context,
                    R.color.red
                )
            )
        }
        binding.textImportanceBody.text = importStr

        // Load deadline
        binding.textViewSelectedDeadline.text = todoItem.deadlineData

        if(todoItem.deadlineData != ""){
            binding.switcherDeadline.isChecked = true
        }

    }

    // Set onClickListener for button "СОХРАНИТЬ"
    private fun connectButtonSave(){
        binding.buttonSave.setOnClickListener {

            var importance = binding.textImportanceBody.text.toString()

            // If String has "!!" in prefix, remove it
            if(importance.equals("!!Высокий"))
                importance = importance.drop(2)


            // If editing existing work
            if(viewModel.isCurrEditing() == true){
                val currModel = viewModel.getCurrModel() ?: return@setOnClickListener
                currModel.textCase = binding.editText.text.toString()
                currModel.importance = importance
                currModel.deadlineData = binding.textViewSelectedDeadline.text.toString()

                viewModel.updateTask(currModel)
                viewModel.updateTaskInAdapter(currModel)
            }
            else{ // If creating a new work
                val newWork = TodoItem(
                    0,
                    binding.editText.text.toString(),
                    importance,
                    binding.textViewSelectedDeadline.text.toString(),
                    false
                )
                Toast.makeText(context, newWork.toString(), Toast.LENGTH_SHORT).show()
                viewModel.addTask(newWork)
                viewModel.increaseCompletedTasks(1)
            }

            mPopBackStack()

        }
    }

    // Set onClickListener for button "Удалить"
    private fun connectButtonDelete(){

        // If we creating a new task
        if(viewModel.isCurrEditing() == false){

            // Set Image's and Text's color to GRAY_26
            binding.imageViewDeleteEditWork.setImageResource(R.drawable.delete_gray)
            binding.buttonDeleteEditWork.setTextColor(
                ContextCompat.getColor(
                    binding.imageViewDeleteEditWork.context,
                    R.color.gray_26
                )
            )

            return // button will haven't onClickListener
        }

        binding.buttonDeleteEditWork.setOnClickListener {
            val currTodoItem = viewModel.getCurrModel()
            viewModel.removeTask(currTodoItem!!)
            mPopBackStack()
        }
    }

    // Navigate to back in stack Fragment
    private fun mPopBackStack(){
        view?.findNavController()?.navigateUp()
    }

    // Show dialog with choosing date
    private fun showDateDialog(){

        val currentDate = viewModel.getCurrentDate()

        // Create an instance of DatePickerDialog
        val dpd = DatePickerDialog(
            this@EditWorkFragment.requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Selected Date
                val deadLineString = "" + dayOfMonth + " " + viewModel.getMonthByIndex(monthOfYear) + ", " + year

                // Display Selected Date in textbox
                binding.textViewSelectedDeadline.text = deadLineString

        }, currentDate[0], currentDate[1], currentDate[2])

        dpd.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearCurrModel()
        viewModel.clearCurrEditing()
        _binding = null
    }
}