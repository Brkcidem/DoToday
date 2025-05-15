// Model sınıfı
//Bu sınıf Room veritabanı ile çalışan bir modeldir. Her bir Task (görev), Room'un tablo yapısında bir satırı temsil eder.
package com.burakhancidem.dotoday.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")//Room veritabanında "task_table" adlı bir tablo oluşturur.
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isDone: Boolean = false,
    val timeStamp: Long = System.currentTimeMillis(),//Görevin oluşturulduğu zamanı milisaniye cinsinden tutar.
                                                    //Sıralama, filtreleme vb. için faydalıdır.

    // Haftalık işaretlemeler
    val mondayChecked: Boolean = false,
    val tuesdayChecked: Boolean = false,
    val wednesdayChecked: Boolean = false,
    val thursdayChecked: Boolean = false,
    val fridayChecked: Boolean = false,
    val saturdayChecked: Boolean = false,
    val sundayChecked: Boolean = false
)
