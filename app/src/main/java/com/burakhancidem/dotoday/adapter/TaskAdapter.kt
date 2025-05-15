package com.burakhancidem.dotoday.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.burakhancidem.dotoday.databinding.ItemTaskBinding
import com.burakhancidem.dotoday.model.Task

//RecyclerView için bir adapter tanımlanıyor. taskList, gösterilecek veri listesidir.
class TaskAdapter(private var taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    //Her bir öğe için ViewHolder tanımı yapılır. ViewHolder, XML layout’taki görünümleri tutar.
    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    var onTaskCheckedChangeListener: ((Task) -> Unit)? = null
    var onItemClick: ((Task) -> Unit)? = null
    var onItemLongClick: ((Task) -> Unit)? = null

    //ViewHolder Oluşturma: XML dosyasını (item_task.xml) şişirir ve ViewHolder ile ilişkilendirir.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    //Verileri Görünüme Bağlama: ViewHolder’a veri bağlanır.
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]//İlgili sıradaki görev alınır.
        holder.binding.apply {
            textViewTaskTitle.text = task.title//Başlık ilgili TextView’a yerleştirilir.
            textViewTaskDescription.text = task.description//Açıklama, ilgili TextView’a yerleştirilir.
            checkBoxTask.setOnCheckedChangeListener(null)
            checkBoxTask.isChecked = task.isDone//Checkbox’ın eski dinleyicisi sıfırlanır ve durumu atanır.
            checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
                onTaskCheckedChangeListener?.invoke(task.copy(isDone = isChecked))
            }//Checkbox değiştiğinde ilgili callback tetiklenir.
            root.setOnClickListener { onItemClick?.invoke(task) }
            root.setOnLongClickListener {
                onItemLongClick?.invoke(task)
                true
            }//Satıra tıklama ve uzun basma olayları tetiklenir.
        }
    }

    override fun getItemCount(): Int = taskList.size//Görev listesinin boyutunu döndürür.

    fun updateTaskList(newTaskList: List<Task>) {//Yeni görev listesi ile günceller ve ekranı yeniler.
        taskList = newTaskList
        notifyDataSetChanged()
    }
}
