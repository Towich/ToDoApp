package com.example.todoapp.ui.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import com.example.todoapp.databinding.FragmentStartBinding
import com.example.todoapp.ui.ViewModel.StartViewModel
import com.example.todoapp.ui.gesture.SwipeGesture
import java.lang.Exception
import javax.inject.Inject

/**
 * A fragment which shows and controls the list of tasks.
 */
class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // ViewModel
    private lateinit var viewModel: StartViewModel
    private lateinit var recyclerView: RecyclerView

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
        (requireContext().applicationContext as App).appComponent.startComponent().create().inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(StartViewModel::class.java)

        // Connecting RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Creating Adapter for RecyclerView
        val mAdapter = viewModel.getAdapter()
        recyclerView.adapter = mAdapter

        viewModel.getQuantityOfCompletedTasks()

        // onClick CheckBox on todoItem
        mAdapter.setOnClickListenerCheckBoxButton(object: CustomRecyclerAdapter.OnClickListener {
            override fun onClick(model: TodoItem) {
                if (model.completed) {
                    viewModel.increaseCompletedTasks(1)
                }
                else{
                    viewModel.increaseCompletedTasks(-1)
                }

                viewModel.updateTaskInAdapter(model)
                viewModel.updateTask(model)
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

        // onClick FAB - create a new task
        binding.fab.setOnClickListener {
            view.findNavController().navigate(R.id.action_StartFragment_to_EditWorkFragment)
            viewModel.setCurrEditing(false)
        }

        // onClick "Eye" - hide completed tasks
        binding.imageButtonShowCompletedTasks.setOnClickListener {
            viewModel.showingUncompletedTasks = !viewModel.showingUncompletedTasks

            if(viewModel.showingUncompletedTasks){
                viewModel.setupUncompletedTasks()
                binding.imageButtonShowCompletedTasks.setImageResource(R.drawable.visibility)
            }
            else{
                viewModel.setupTasks()
                binding.imageButtonShowCompletedTasks.setImageResource(R.drawable.visibility_off)
            }
        }

        // LiveData with count of completed tasks
        viewModel.completedTasks.observe(viewLifecycleOwner, Observer {
            val completedTaskString = "Выполнено - " + viewModel.completedTasks.value
            binding.textViewCompletedTasks.text = completedTaskString
        })

        if(viewModel.showingUncompletedTasks)
            viewModel.setupUncompletedTasks()
        else
            viewModel.setupTasks()

        hideCompletedTasksOnToolBar()
        swipeToGesture(recyclerView)
    }

    // Tracking the gesture "swiping left"
    // - then task has swiped left, removing it
    private fun swipeToGesture(itemRecyclerView: RecyclerView?){

        val swipeGesture = object : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.absoluteAdapterPosition
                val actionBtnTapped = false

                try{
                    when(direction){

                        ItemTouchHelper.LEFT->{
                            val deleteItem = viewModel.getAdapter().getTasks()[position]
                            viewModel.removeTask(deleteItem)

                            if(deleteItem.completed)
                                viewModel.increaseCompletedTasks(-1)
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

    // Hide TextView "Completed Tasks"
    // then toolbar is collapsed
    private fun hideCompletedTasksOnToolBar(){
        binding.appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
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

    // Destructor
    // - clear binding;
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}