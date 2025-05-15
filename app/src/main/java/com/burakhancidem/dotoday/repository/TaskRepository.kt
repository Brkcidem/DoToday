//Veri Katmanı Aracısı (Repository Pattern)
//Repository, ViewModel ile DAO arasında bir köprü görevi görür ve genellikle iş mantığı burada yazılır.
//Amacı, ViewModel'in doğrudan DAO veya başka veri sağlayıcılarla ilgilenmemesini sağlamaktır.
package com.burakhancidem.dotoday.repository

import androidx.lifecycle.LiveData
import com.burakhancidem.dotoday.data.TaskDao
import com.burakhancidem.dotoday.model.Task

class TaskRepository(private val taskDao: TaskDao) {//Sadece TaskDao alır, dışarıya DAO'nun ne yaptığıyla ilgilenmeden hizmet sunar.

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()//DAO’dan dönen LiveData’yı aynen ViewModel'e aktarır.

    //Aşağıdaki metotlar DAO’daki işlemleri çağırır ama ViewModel’in DAO ile doğrudan çalışmasını engeller.

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    // 🔄 Günlük işaretlemeleri sıfırlar (örneğin her günün başında)
    suspend fun resetAllDailyChecks() {
        taskDao.resetAllDailyChecks()
    }

    // 🔁 Haftalık işaretlemeleri sıfırlar (örneğin haftalık rapor ekranı temizlenirken)
    suspend fun resetAllWeeklyChecks() {
        taskDao.resetAllWeeklyChecks()
    }

    // ✅ O gün isDone true olan görevlerin haftalık kutusunu işaretler
    suspend fun markTodayAsChecked() {
        taskDao.markTodayAsChecked()
    }
}

/*
    Neden Repository Kullanılır?

    Ayrıştırma (Separation of Concerns): UI mantığını veri erişiminden ayırır.

    Test edilebilirlik: ViewModel’ler test edilirken sahte Repository kullanılabilir.

    Genişleyebilirlik: İleride API, cache gibi kaynaklar eklenirse burada birleştirilebilir.
    (örneğin bir görev hem API'den hem Room’dan geliyorsa, burada karar verilebilir).
 */