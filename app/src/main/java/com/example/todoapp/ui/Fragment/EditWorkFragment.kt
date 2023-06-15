package com.example.todoapp.ui.Fragment

import android.app.DatePickerDialog
import android.graphics.PorterDuff
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
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.source.StartDataSource
import com.example.todoapp.databinding.FragmentEditWorkBinding
import com.example.todoapp.ui.ViewModel.StartViewModel
import java.util.Calendar
import javax.xml.datatype.DatatypeConstants.MONTHS

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditWorkFragment : Fragment() {


    private var _binding: FragmentEditWorkBinding? = null
    private val startViewModel: StartViewModel by activityViewModels()
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

        binding.imagebuttonClose.setOnClickListener {
            mPopBackStack()
        }

        val buttonShowImportancePopupMenu = binding.textImportanceTitle

        binding.textImportanceTitle.setOnClickListener {
            showImportancePopupMenu(buttonShowImportancePopupMenu)
        }

        binding.buttonSetDeadline.isEnabled = false
        binding.buttonSetDeadline.setOnClickListener {
            showDateDialog()
        }

        binding.switcherDeadline.setOnClickListener {
            if(binding.switcherDeadline.isChecked){
                binding.buttonSetDeadline.isEnabled = true
                Toast.makeText(context, "NOW ENABLED!", Toast.LENGTH_SHORT).show()
                showDateDialog()
            }
            else{
                binding.buttonSetDeadline.isEnabled = false
                binding.textViewSelectedDeadline.text = ""
                startViewModel.clearDeadline()
                Toast.makeText(context, "NOW DISABLED!", Toast.LENGTH_SHORT).show()
            }
        }

        connectButtonSave()
        connectButtonDelete()
        loadEditingModel()
    }

    // Show Popup menu with choosing importance
    private fun showImportancePopupMenu(v: View){
        val popupMenu = PopupMenu(context, v)
        popupMenu.menuInflater.inflate(R.menu.menu_importance, popupMenu.menu)

        var highImportanceItem = popupMenu.menu.getItem(2)
        var spannable: SpannableString = SpannableString(highImportanceItem.title.toString())
        spannable.setSpan(ForegroundColorSpan(
            ContextCompat.getColor(v.context, R.color.red)),
            0,
            spannable.length,
            0
        )
        highImportanceItem.title = spannable

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem: MenuItem? ->

//            when(menuItem!!.itemId){
//                R.id.menu_importance_no -> {
//                    Toast.makeText(context, "menu_importance_no", Toast.LENGTH_SHORT).show()
//                }
//                R.id.menu_importance_low -> {
//                    Toast.makeText(context, "menu_importance_low", Toast.LENGTH_SHORT).show()
//                }
//                R.id.menu_importance_high -> {
//                    Toast.makeText(context, "menu_importance_high", Toast.LENGTH_SHORT).show()
//                }
//            }

            binding.textImportanceBody.text = menuItem?.title

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
        val todoItem = startViewModel.getCurrModel() ?: return

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

            if(importance.equals("!!Высокий"))
                importance = importance.drop(2)

            // edit existing work
            if(startViewModel.isCurrEditing() == true){
                startViewModel.getCurrModel()?.textCase = binding.editText.text.toString()
                startViewModel.getCurrModel()?.importance = importance
                startViewModel.getCurrModel()?.deadlineData = binding.textViewSelectedDeadline.text.toString()
            }
            else{ // create a new work
                val newWork = TodoItem(binding.editText.text.toString(), importance, binding.textViewSelectedDeadline.text.toString())
                Toast.makeText(context, newWork.toString(), Toast.LENGTH_SHORT).show()
                startViewModel.addWork(newWork)
            }

            mPopBackStack()

        }
    }

    // Set onClickListener for button "Удалить"
    private fun connectButtonDelete(){

        // If we creating a new task
        if(startViewModel.isCurrEditing() == false){

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

        // If we editing an existing task
        binding.buttonDeleteEditWork.setOnClickListener {
            startViewModel.getCurrModel()?.let { it1 -> startViewModel.removeWork(it1) }
            mPopBackStack()
        }
    }

    private fun mPopBackStack(){
        view?.findNavController()?.navigateUp()
    }

    private fun showDateDialog(){

        val currentDate = startViewModel.getCurrentDate()

        val dpd = DatePickerDialog(
            this@EditWorkFragment.requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Selected Date
                val deadLineString = "" + dayOfMonth + " " + startViewModel.getMonthByIndex(monthOfYear) + ", " + year

                // Display Selected Date in textbox
                binding.textViewSelectedDeadline.text = deadLineString

        }, currentDate[0], currentDate[1], currentDate[2])

        dpd.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        startViewModel.clearCurrModel()
        startViewModel.clearCurrEditing()
        _binding = null
    }
}