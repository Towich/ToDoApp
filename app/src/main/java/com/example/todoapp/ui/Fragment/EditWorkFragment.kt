package com.example.todoapp.ui.Fragment

import android.graphics.Color
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
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.databinding.FragmentEditWorkBinding
import com.example.todoapp.ui.ViewModel.StartViewModel

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
            requireActivity().supportFragmentManager.popBackStack();
        }

        val buttonShowImportancePopupMenu = binding.textImportanceTitle

        binding.textImportanceTitle.setOnClickListener {
            showImportancePopupMenu(buttonShowImportancePopupMenu)
        }

        binding.buttonSave.setOnClickListener {
            startViewModel.addWork(TodoItem("newWork", "СРОЧНО"))
            requireActivity().supportFragmentManager.popBackStack();
        }
    }

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

            when(menuItem!!.itemId){
                R.id.menu_importance_no -> {
                    Toast.makeText(context, "menu_importance_no", Toast.LENGTH_SHORT).show()
                }
                R.id.menu_importance_low -> {
                    Toast.makeText(context, "menu_importance_low", Toast.LENGTH_SHORT).show()
                }
                R.id.menu_importance_high -> {
                    Toast.makeText(context, "menu_importance_high", Toast.LENGTH_SHORT).show()
                }
            }

            binding.textImportanceBody.text = menuItem.title
            true
        })

        popupMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}