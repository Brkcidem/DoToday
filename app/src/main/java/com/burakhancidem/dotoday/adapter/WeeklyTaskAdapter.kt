package com.burakhancidem.dotoday.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.burakhancidem.dotoday.R
import com.burakhancidem.dotoday.databinding.ItemWeeklyTaskBinding
import com.burakhancidem.dotoday.model.Task
import java.util.*

class WeeklyTaskAdapter(
    private var taskList: List<Task>,
    private val onDayToggle: (task: Task, dayIndex: Int, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<WeeklyTaskAdapter.WeeklyTaskViewHolder>() {

    inner class WeeklyTaskViewHolder(val binding: ItemWeeklyTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyTaskViewHolder {
        val binding = ItemWeeklyTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeeklyTaskViewHolder(binding)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: WeeklyTaskViewHolder, position: Int) {
        val task = taskList[position]
        val todayIndex = getTodayIndex()

        with(holder.binding) {
            textViewTaskTitle.text = task.title

            val checkedStates = listOf(
                task.mondayChecked,
                task.tuesdayChecked,
                task.wednesdayChecked,
                task.thursdayChecked,
                task.fridayChecked,
                task.saturdayChecked,
                task.sundayChecked
            )

            val imageViews = listOf(
                imageViewMonday,
                imageViewTuesday,
                imageViewWednesday,
                imageViewThursday,
                imageViewFriday,
                imageViewSaturday,
                imageViewSunday
            )

            for (i in imageViews.indices) {
                val isChecked = checkedStates[i]
                val isPastOrToday = i <= todayIndex

                updateDayIcon(imageViews[i], isChecked, i < todayIndex)

                imageViews[i].isClickable = isPastOrToday
                imageViews[i].alpha = if (isPastOrToday) 1f else 0.3f

                if (isPastOrToday) {
                    imageViews[i].setOnClickListener {
                        val currentChecked = checkedStates[i]
                        onDayToggle(task, i, !currentChecked)
                    }
                } else {
                    imageViews[i].setOnClickListener(null)
                }
            }

        }
    }

    fun updateTaskList(newList: List<Task>) {
        taskList = newList
        notifyDataSetChanged()
    }

    private fun getTodayIndex(): Int {
        return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> -1
        }
    }

    private fun updateDayIcon(
        imageView: ImageView,
        isChecked: Boolean,
        isPastDay: Boolean
    ) {
        val resId = when {
            isChecked -> R.drawable.baseline_check_24
            isPastDay -> R.drawable.baseline_close_24
            else -> R.drawable.baseline_crop_square_24
        }
        imageView.setImageResource(resId)
    }
}
