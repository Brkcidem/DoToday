package com.burakhancidem.dotoday.ui

import TaskViewModelFactory
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.burakhancidem.dotoday.DoTodayApplication
import com.burakhancidem.dotoday.R
import com.burakhancidem.dotoday.adapter.TaskAdapter
import com.burakhancidem.dotoday.databinding.FragmentTaskListBinding
import com.burakhancidem.dotoday.viewmodel.TaskViewModel
import java.util.Calendar

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null//_binding: Geçici (nullable) binding nesnesi.
    private val binding get() = _binding!!//Null kontrolü yapılmış güvenli erişim.

    private val taskViewModel: TaskViewModel by viewModels {//Açıklamsı detaylıca en aşağıda
        TaskViewModelFactory((requireActivity().application as DoTodayApplication).repository, this)
    }

    private val taskAdapter = TaskAdapter(emptyList())//Listeyi gösterecek olan adaptör başlangıçta boş bir liste ile oluşturulmuştur.

    override fun onCreateView(//Fragment ilk kez oluşturulurken layout dosyası buradan bağlanır. FragmentTaskListBinding nesnesi burada başlatılır.
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {//View hazırlandıktan sonra yapılacak işlemler burada sıralanır
        super.onViewCreated(view, savedInstanceState)
        checkForNewDay()//Yeni bir gün mü kontrolü
        setupRecyclerView()//RecyclerView kurulumu
        setupObservers()//LiveData gözlemi
        setupClickListeners()//FAB tıklama olayları
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTasks.adapter = taskAdapter

        taskAdapter.onTaskCheckedChangeListener = { updatedTask ->
            taskViewModel.updateTask(updatedTask)
            taskViewModel.markTodayAsChecked()
        }

        taskAdapter.onItemClick = { task ->
            val action = TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment(task.id)
            findNavController().navigate(action)
        }

        taskAdapter.onItemLongClick = { task ->
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_task_title))
                .setMessage(getString(R.string.delete_task_message))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    taskViewModel.deleteTask(task)
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }
    }

    private fun setupObservers() {
        taskViewModel.allTasks.observe(viewLifecycleOwner, Observer { taskList ->
            if (taskList.isEmpty()) {
                binding.noTasksMessage.visibility = View.VISIBLE
                binding.recyclerViewTasks.visibility = View.GONE
            } else {
                binding.noTasksMessage.visibility = View.GONE
                binding.recyclerViewTasks.visibility = View.VISIBLE
                taskAdapter.updateTaskList(taskList)
            }
        })
    }

    private fun setupClickListeners() {
        binding.fabAddTask.setOnClickListener {
            val action = TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment(-1)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {//Bellek Sızıntısını Önleme
        super.onDestroyView()
        _binding = null//Binding nesnesi null yapılır, aksi halde bellekte tutulmaya devam ederdi.
    }

    // 🔁 GÜN KONTROLÜ – isDone günlük kontroller sıfırlanmalı mı?
    private fun checkForNewDay() {
        val sharedPrefs = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val lastOpenedDay = sharedPrefs?.getInt("lastOpenedDay", -1)
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

        if (lastOpenedDay != currentDay) {
            taskViewModel.markTodayAsChecked()
            taskViewModel.resetAllDailyChecks()
            sharedPrefs?.edit()?.putInt("lastOpenedDay", currentDay)?.apply()
        }
    }
}

/*
TaskListFragment.kt içinde ViewModel kullanımı:

private val taskViewModel: TaskViewModel
→ Fragment içinde erişilecek ViewModel örneğini tanımlıyorsun. Bu, veri ile UI arasındaki köprüyü sağlar.

by viewModels { ... }
→ Bu bir Kotlin property delegate’idir. ViewModel’in yaşam döngüsünü Fragment’a bağlar. Fragment yok olursa ViewModel da temizlenir.

TaskViewModelFactory(...)
→ Bu fabrika sayesinde TaskViewModel’e TaskRepository ve SavedStateHandle gibi dış bağımlılıkları aktarabiliyorsun.
  ViewModel'ler normalde constructor'da parametre alamaz; bu yüzden Factory ile çözülür.

requireActivity().application as DoTodayApplication
→ Uygulamanın Application sınıfına ulaşıp içindeki repository'i alıyorsun. DoTodayApplication içinde tanımlanmıştı

this
→ Bu SavedStateRegistryOwner’dır, yani Fragment’in kendisi. SavedStateHandle kullanımına izin verir.
 */