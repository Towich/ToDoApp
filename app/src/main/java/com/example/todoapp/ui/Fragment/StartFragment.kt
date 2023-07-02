package com.example.todoapp.ui.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import com.example.todoapp.databinding.FragmentStartBinding
import com.example.todoapp.ui.ViewModel.StartViewModel
import com.example.todoapp.ui.gesture.SwipeGesture
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val viewModel: StartViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private var showingUncompletedTasks: Boolean = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        val mAdapter = viewModel.getAdapter()
        recyclerView.adapter = mAdapter

        updateCounterUncompletedTasks()

        // onClick CheckBox on todoItem
        mAdapter.setOnClickListenerCheckBoxButton(object: CustomRecyclerAdapter.OnClickListener {
            override fun onClick(model: TodoItem) {
                if (model.completed) {
                    viewModel.increaseCompletedTasks(1)
                }
                else{
                    viewModel.increaseCompletedTasks(-1)
                }

                viewModel.updateTask(model)
                updateCounterUncompletedTasks()
            }
        })

        // onClick Button "Info" on todoItem
        mAdapter.setOnClickListenerInfoButton(object: CustomRecyclerAdapter.OnClickListener {
            override fun onClick(model: TodoItem) {
                viewModel.setCurrModel(model)
                viewModel.setCurrEditing(true)
                view.findNavController().navigate(R.id.action_StartFragment_to_EditWorkFragment)
            }
        })

        // onClick FAB
        binding.fab.setOnClickListener {
            view.findNavController().navigate(R.id.action_StartFragment_to_EditWorkFragment)
            viewModel.setCurrEditing(false)
        }

        // onClick "Eye"
        binding.imageButtonShowCompletedTasks.setOnClickListener {
            showingUncompletedTasks = !showingUncompletedTasks

            if(showingUncompletedTasks){
                viewModel.setupUncompletedTasks()
                binding.imageButtonShowCompletedTasks.setImageResource(R.drawable.visibility)
            }
            else{
                viewModel.setupTasks()
                binding.imageButtonShowCompletedTasks.setImageResource(R.drawable.visibility_off)
            }
        }

        viewModel.setupTasks()
        hideCompletedTasksOnToolBar()
        swipeToGesture(recyclerView)
    }

    private fun swipeToGesture(itemRecyclerView: RecyclerView?){

        val swipeGesture = object : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.absoluteAdapterPosition
                val actionBtnTapped = false

                try{
                    when(direction){

                        ItemTouchHelper.LEFT->{
                            val deleteItem = viewModel.getWork(position)
                            viewModel.removeTask(deleteItem)

                            if(deleteItem.completed)
                                viewModel.increaseCompletedTasks(-1)

                            updateCounterUncompletedTasks()
                        }

                    }
                }
                catch (e: Exception){
                    Toast.makeText(this@StartFragment.requireContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)

        touchHelper.attachToRecyclerView(itemRecyclerView)
    }

    private fun updateCounterUncompletedTasks(){
        val completedTaskString = "Выполнено - " + viewModel.getCompletedTasks()
        binding.textViewCompletedTasks.text = completedTaskString
    }

    private fun hideCompletedTasksOnToolBar(){
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            run {
                if (verticalOffset == 0) {
                    // If expanded, then do this
                    binding.textViewCompletedTasks.visibility = View.VISIBLE
                }
                else {
                    // If collapsed || somewhere in between
                    binding.textViewCompletedTasks.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}