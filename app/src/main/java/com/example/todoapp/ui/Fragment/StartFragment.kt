package com.example.todoapp.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.databinding.FragmentStartBinding
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import com.example.todoapp.ui.ViewModel.StartViewModel
import com.example.todoapp.ui.gesture.SwipeGesture
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

    var toolBarExpanded: Boolean = true
    var toolBarCollapsed: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inject
        (requireContext().applicationContext as App).appComponent.startComponent().create()
            .inject(this)

        // Instantiate ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory)[StartViewModel::class.java]

        // Connecting RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Creating Adapter for RecyclerView
        val mAdapter = viewModel.getAdapter()
        recyclerView.adapter = mAdapter

        // Setup counter of already completed tasks
        viewModel.getQuantityOfCompletedTasks()

        // onClick CheckBox on todoItem
        mAdapter.setOnClickListenerCheckBoxButton(object : CustomRecyclerAdapter.OnClickListener {
            override fun onClick(model: TodoItem) {
                if (model.completed) {
                    viewModel.increaseCompletedTasks(1)
                } else {
                    viewModel.increaseCompletedTasks(-1)
                }

                viewModel.updateTaskInAdapter(model)
                viewModel.updateTask(model)
            }
        })

        // onClick Button "Info" on todoItem
        mAdapter.setOnClickListenerInfoButton(object : CustomRecyclerAdapter.OnClickListener {
            override fun onClick(model: TodoItem) {
                viewModel.setCurrModel(model)
                viewModel.setCurrEditing(true)
                view.findNavController().navigate(R.id.action_StartFragment_to_editTaskFragment)
            }
        })

        // onClick FAB - create a new task
        binding.fab.setOnClickListener {
            view.findNavController().navigate(R.id.action_StartFragment_to_editTaskFragment)
            viewModel.setCurrEditing(false)
        }

        // onClick "Eye" - hide completed tasks
        binding.imageButtonShowCompletedTasks.setOnClickListener {
            viewModel.showingUncompletedTasks = !viewModel.showingUncompletedTasks

            if (viewModel.showingUncompletedTasks) {
                viewModel.setupUncompletedTasks()
                binding.imageButtonShowCompletedTasks.setImageResource(R.drawable.visibility)
            } else {
                viewModel.setupTasks()
                binding.imageButtonShowCompletedTasks.setImageResource(R.drawable.visibility_off)
            }
        }

        // LiveData with count of completed tasks
        viewModel.completedTasks.observe(viewLifecycleOwner) {
            val completedTaskString = "Выполнено - " + viewModel.completedTasks.value
            binding.textViewCompletedTasks.text = completedTaskString
        }

        if (viewModel.showingUncompletedTasks) {
            viewModel.setupUncompletedTasks()
            binding.imageButtonShowCompletedTasks.setImageResource(R.drawable.visibility)
        } else {
            viewModel.setupTasks()
            binding.imageButtonShowCompletedTasks.setImageResource(R.drawable.visibility_off)
        }

        appBarOnOffsetChanged()
        swipeToGesture(recyclerView)
    }

    // Tracking the gesture "swiping left"
    // then task has swiped left, removing it
    private fun swipeToGesture(itemRecyclerView: RecyclerView?) {

        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.absoluteAdapterPosition

                // Get todoItem to remove
                val deleteItem = viewModel.getAdapter().getTasks()[position]

                try {
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            // Remove task from DB
                            viewModel.removeTask(deleteItem)

                            if (deleteItem.completed)
                                viewModel.increaseCompletedTasks(-1)

                            // Decrease height RecyclerView with animation
                            viewModel.animateNewHeight(
                                recyclerView,
                                recyclerView.measuredHeight - viewHolder.itemView.measuredHeight
                            )

                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@StartFragment.requireContext(),
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                // Show Snackbar
                viewModel.showSnackbarCancelRemove(
                    requireContext().applicationContext,
                    itemRecyclerView!!,
                    deleteItem
                ) {
                    // If action button has pressed
                    // Increase RecyclerView's height
                    viewModel.animateNewHeight(
                        recyclerView,
                        recyclerView.measuredHeight + viewHolder.itemView.measuredHeight
                    )
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)

        touchHelper.attachToRecyclerView(itemRecyclerView)
    }

    // Animate TextView "Completed Tasks" and Button "Eye"
    // when toolbar is extended/collapsed
    private fun appBarOnOffsetChanged() {
        val marginEndAnimationDuration = 500L
        val alphaAnimationDuration = 250L
        val expandedEndMargin = 70
        val collapsedEndMargin = 20

        binding.appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            run {
                // Animate TextView and EyeButton when $verticalOffset is close to be extended
                if (verticalOffset > -5) {
                    // If expanded
                    if (toolBarExpanded) {

                        // Animate alpha of TextView "Completed tasks"
                        viewModel.animateAlpha(
                            binding.textViewCompletedTasks,
                            1f,
                            alphaAnimationDuration
                        )

                        // Animate marginEnd of Button "Eye"
                        viewModel.animateMarginEyeButton(
                            binding.imageButtonShowCompletedTasks,
                            expandedEndMargin,
                            marginEndAnimationDuration
                        )
                        toolBarExpanded = false
                    }
                } else {
                    // If somewhere in between
                    if (!toolBarExpanded && verticalOffset < -5) {

                        // Animate alpha of TextView "Completed tasks"
                        viewModel.animateAlpha(
                            binding.textViewCompletedTasks,
                            0f,
                            alphaAnimationDuration
                        )

                        // Animate marginEnd of Button "Eye"
                        viewModel.animateMarginEyeButton(
                            binding.imageButtonShowCompletedTasks,
                            collapsedEndMargin,
                            marginEndAnimationDuration
                        )
                        toolBarExpanded = true
                    }
                }

                // Like above, animate when $verticalOffset is close to be collapsed
                if (verticalOffset < -270) {
                    // If collapsed
                    if (toolBarCollapsed) {

                        // Animate marginEnd of Button "Eye"
                        viewModel.animateMarginEyeButton(
                            binding.imageButtonShowCompletedTasks,
                            collapsedEndMargin,
                            marginEndAnimationDuration
                        )
                        toolBarCollapsed = false
                    }
                } else {
                    // If somewhere in between
                    if (!toolBarCollapsed && verticalOffset > -270) {

                        // Animate marginEnd of Button "Eye"
                        viewModel.animateMarginEyeButton(
                            binding.imageButtonShowCompletedTasks,
                            expandedEndMargin,
                            marginEndAnimationDuration
                        )
                        toolBarCollapsed = true
                    }
                }
            }
        }
    }

    // Destructor
    // - clear binding;
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.dismissShowingSnackbar()
    }
}