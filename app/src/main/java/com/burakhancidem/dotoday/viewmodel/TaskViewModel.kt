//UI ile Veri Katmanı(repository) Arasındaki Köprüdür
//ViewModel, UI (fragment veya activity) ile veri kaynakları (Room, API, vb.) arasında çalışır.
//UI ile doğrudan Room arasında bağlantı kurmak yerine ViewModel üzerinden yapılır.
//Bu sayede veri yaşam döngüsüne duyarlı olur ve UI yeniden oluşturulduğunda veri kaybolmaz.
package com.burakhancidem.dotoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burakhancidem.dotoday.model.Task
import com.burakhancidem.dotoday.repository.TaskRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskViewModel(
    private val repository: TaskRepository,//Repository sınıfı üzerinden veriler yönetilir
    private val savedStateHandle: SavedStateHandle//UI durumunu korumak için (örn. ekran döndüğünde veriler korunur).
) : ViewModel() {

    val allTasks: LiveData<List<Task>> = repository.allTasks//Repository'den tüm görevleri LiveData olarak alır.
                          // Böylece Fragment ya da Activity bu veriyi gözlemleyip her değişiklikte güncellenir.

    fun insertTask(task: Task) {//Coroutine içinde arka planda yeni görev eklenir. UI donmaz.
        viewModelScope.launch {//Coroutine başlatır, böylece UI donmaz.
            repository.insertTask(task)//Arka planda DAO'ya yönlendirilerek veritabanına kayıt yapılır.
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    // Günlük tamamlanan görevleri sıfırlar (her yeni gün başlangıcında çağrılır)
    fun resetAllDailyChecks() {
        viewModelScope.launch {
            repository.resetAllDailyChecks()
        }
    }

    // Haftalık görünümdeki işaretlemeleri sıfırlar (örneğin yeni haftaya geçerken)
    fun resetAllWeeklyChecks() {
        viewModelScope.launch {
            repository.resetAllWeeklyChecks()
        }
    }

    // Günlük işaretli olanları bugünkü haftalık kutucuğa işler
    fun markTodayAsChecked() {
        viewModelScope.launch {
            repository.markTodayAsChecked()
        }
    }

    fun updateDayChecked(dayIndex: Int, taskId: Int, isChecked: Boolean) {
        viewModelScope.launch {
            val currentTask = allTasks.value?.find { it.id == taskId }
            currentTask?.let { task ->
                val updatedTask = when (dayIndex) {
                    0 -> task.copy(mondayChecked = isChecked)
                    1 -> task.copy(tuesdayChecked = isChecked)
                    2 -> task.copy(wednesdayChecked = isChecked)
                    3 -> task.copy(thursdayChecked = isChecked)
                    4 -> task.copy(fridayChecked = isChecked)
                    5 -> task.copy(saturdayChecked = isChecked)
                    6 -> task.copy(sundayChecked = isChecked)
                    else -> task
                }

                val todayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK).let {
                    when (it) {
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

                val finalTask = if (dayIndex == todayIndex) {
                    updatedTask.copy(isDone = isChecked)
                } else {
                    updatedTask
                }

                repository.updateTask(finalTask)
            }
        }
    }


}

/*
    ->Neden viewModelScope.launch?
    **ViewModel'e özel coroutine scope sağlar. ViewModel yok edildiğinde otomatik olarak coroutine de iptal edilir.
    **Bu da bellek sızıntılarını önler.

    ->DAO neden doğrudan çağrılmaz da Repository üzerinden çağrılır?
    **Katmanlı mimari sağlar (Separation of Concerns).
    **Test edilebilirlik artar.
    **Kod tekrarını azaltır.
 */