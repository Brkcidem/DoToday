// Görev ekleme fragment sınıfı
package com.burakhancidem.dotoday.ui

import TaskViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.burakhancidem.dotoday.DoTodayApplication
import com.burakhancidem.dotoday.databinding.FragmentAddTaskBinding
import com.burakhancidem.dotoday.model.Task
import com.burakhancidem.dotoday.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val args: AddTaskFragmentArgs by navArgs()//Navigation argümanı (örneğin görev ID’si) burada alınır.

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((requireActivity().application as DoTodayApplication).repository, this)
    }

    private var currentTask: Task? = null//Eğer bir görev düzenleniyorsa, bu değişkende tutulur.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskId = args.taskId
        if (taskId != -1) {
            taskViewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
                currentTask = tasks.find { it.id == taskId }
                currentTask?.let {
                    binding.editTextTaskTitle.setText(it.title)
                    binding.editTextTaskDescription.setText(it.description)
                }
            }
        }

        binding.buttonSaveTask.setOnClickListener {
            val title = binding.editTextTaskTitle.text.toString()
            val description = binding.editTextTaskDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                lifecycleScope.launch {
                    val updatedTask = currentTask?.copy(
                        title = title,
                        description = description
                    ) ?: Task(title = title, description = description)

                    if (currentTask != null) {
                        taskViewModel.updateTask(updatedTask)
                    } else {
                        taskViewModel.insertTask(updatedTask)
                    }

                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}