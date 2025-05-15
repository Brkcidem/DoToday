package com.burakhancidem.dotoday.ui

import TaskViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.burakhancidem.dotoday.DoTodayApplication
import com.burakhancidem.dotoday.adapter.WeeklyTaskAdapter
import com.burakhancidem.dotoday.databinding.FragmentWeeklyOverviewBinding
import com.burakhancidem.dotoday.viewmodel.TaskViewModel

class WeeklyOverviewFragment : Fragment() {

    private var _binding: FragmentWeeklyOverviewBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((requireActivity().application as DoTodayApplication).repository, this)
    }

    private lateinit var weeklyTaskAdapter: WeeklyTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeeklyOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeTasks()
    }

    private fun setupRecyclerView() {
        weeklyTaskAdapter = WeeklyTaskAdapter(emptyList()) { task, dayIndex, isChecked ->
            taskViewModel.updateDayChecked(dayIndex, task.id, isChecked)
        }

        binding.recyclerViewWeeklyTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = weeklyTaskAdapter
        }
    }

    private fun observeTasks() {
        taskViewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            weeklyTaskAdapter.updateTaskList(tasks)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
