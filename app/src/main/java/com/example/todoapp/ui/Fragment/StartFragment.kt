package com.example.todoapp.ui.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.ui.Adapter.CustomRecyclerAdapter
import com.example.todoapp.ui.Repository.StartRepository
import com.example.todoapp.databinding.FragmentStartBinding
import com.example.todoapp.ui.ViewModel.StartViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val viewModel: StartViewModel by activityViewModels()

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

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModel.getWorks().value?.let { CustomRecyclerAdapter(it) }

        binding.fab.setOnClickListener {
            view.findNavController().navigate(R.id.EditWorkFragment)
        }

        viewModel.getWorks().observe(viewLifecycleOwner, Observer { it?.let {
            recyclerView.adapter?.notifyDataSetChanged()
        } })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}