// Uygulama sınıfı
package com.burakhancidem.dotoday

import android.app.Application // Application temel sınıfı
import com.burakhancidem.dotoday.data.TaskDatabase // Veritabanı sınıfı
import com.burakhancidem.dotoday.repository.TaskRepository // Repository sınıfı

class DoTodayApplication : Application() {
    val repository: TaskRepository by lazy {//lazy ile Room ve Repository sadece ihtiyaç halinde oluşturulur (memory-safe).
        TaskRepository(TaskDatabase.getDatabase(this).taskDao())
    }
}
