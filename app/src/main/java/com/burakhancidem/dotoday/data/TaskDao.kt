//Data Access Object (Veri Erişim Katmanı)
//TaskDao, Room veritabanı ile uygulamanın veri katmanı (repository) arasında köprü kurar.
//Veritabanı işlemlerini tanımlar ama uygulamaz – uygulamayı Room otomatik olarak üretir.
package com.burakhancidem.dotoday.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.burakhancidem.dotoday.model.Task

@Dao//Bu arayüzün bir Data Access Object olduğunu belirtir.
    //Room, bu arayüzü kullanarak otomatik olarak veri erişim kodlarını üretir.
interface TaskDao {
    @Query("SELECT * FROM task_table ORDER BY timeStamp DESC")
    fun getAllTasks(): LiveData<List<Task>>//Dönen veri LiveData olduğu için UI otomatik olarak güncellenir.

    @Insert
    suspend fun insertTask(task: Task)//suspend: Coroutine içinde çalıştırılması gerektiğini belirtir,
                                      //böylece işlem arka planda yapılır ve UI donmaz.

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE task_table SET isDone = 0")
    suspend fun resetAllDailyChecks()

    @Query("""
        UPDATE task_table 
        SET 
            mondayChecked = 0,
            tuesdayChecked = 0,
            wednesdayChecked = 0,
            thursdayChecked = 0,
            fridayChecked = 0,
            saturdayChecked = 0,
            sundayChecked = 0
    """)
    suspend fun resetAllWeeklyChecks()

    @Query("""
        UPDATE task_table
        SET 
            mondayChecked = CASE WHEN strftime('%w', 'now') = '1' THEN isDone ELSE mondayChecked END,
            tuesdayChecked = CASE WHEN strftime('%w', 'now') = '2' THEN isDone ELSE tuesdayChecked END,
            wednesdayChecked = CASE WHEN strftime('%w', 'now') = '3' THEN isDone ELSE wednesdayChecked END,
            thursdayChecked = CASE WHEN strftime('%w', 'now') = '4' THEN isDone ELSE thursdayChecked END,
            fridayChecked = CASE WHEN strftime('%w', 'now') = '5' THEN isDone ELSE fridayChecked END,
            saturdayChecked = CASE WHEN strftime('%w', 'now') = '6' THEN isDone ELSE saturdayChecked END,
            sundayChecked = CASE WHEN strftime('%w', 'now') = '0' THEN isDone ELSE sundayChecked END
    """)
    suspend fun markTodayAsChecked()
}