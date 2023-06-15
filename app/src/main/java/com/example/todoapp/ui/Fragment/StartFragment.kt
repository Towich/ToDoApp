package com.example.todoapp.ui.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val viewModel: StartViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        val mAdapter = CustomRecyclerAdapter(viewModel.getWorks())
        recyclerView.adapter = mAdapter

        mAdapter?.setOnClickListener(object: CustomRecyclerAdapter.OnClickListener {
            override fun onClick(model: TodoItem) {
                viewModel.setCurrModel(model)
                viewModel.setCurrEditing(true)
                view.findNavController().navigate(R.id.action_StartFragment_to_EditWorkFragment)
            }
        })

        binding.fab.setOnClickListener {
            view.findNavController().navigate(R.id.action_StartFragment_to_EditWorkFragment)
            viewModel.setCurrEditing(false)
        }

        viewModel.getWorks().observe(viewLifecycleOwner, Observer { it?.let {
            mAdapter?.notifyDataSetChanged()
            Toast.makeText(context, "UPDATING RECYCLER_VIEW ADAPTER", Toast.LENGTH_SHORT).show()
        } })

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
                            if (deleteItem != null) {
                                viewModel.removeWork(deleteItem)
                            }

                            val snackBar = Snackbar.make(this@StartFragment.recyclerView, "Item deleted", Snackbar.LENGTH_SHORT)
                                .addCallback(object :BaseTransientBottomBar.BaseCallback<Snackbar>(){
                                    override fun onShown(transientBottomBar: Snackbar?) {
                                        super.onShown(transientBottomBar)

                                        transientBottomBar?.setAction("UNDO"){

                                        }
                                    }
                                })
                                .apply {
                                    animationMode = Snackbar.ANIMATION_MODE_FADE
                                }

                            snackBar.setActionTextColor(
                                ContextCompat.getColor(
                                    this@StartFragment.requireContext(),
                                    R.color.green
                                )
                            )
                            snackBar.show()
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

        recyclerView.adapter?.notifyDataSetChanged()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}