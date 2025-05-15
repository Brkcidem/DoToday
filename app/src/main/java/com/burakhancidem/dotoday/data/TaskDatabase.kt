//Room Veritabanı Tanımı
//Bu sınıf, Room veritabanının tek bir örnek (singleton) olarak oluşturulmasını ve uygulamanın her yerinden erişilmesini sağlar.
package com.burakhancidem.dotoday.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.burakhancidem.dotoday.model.Task

// Veritabanı yapılandırması
@Database(entities = [Task::class], version = 2, exportSchema = false)//Bu sınıfın bir Room veritabanı olduğunu belirtir.
                                                                      //Bu veritabanı Task adlı tabloyu (Entity) içerir.

abstract class TaskDatabase : RoomDatabase() {//Room bu sınıftan otomatik olarak bir alt sınıf üretir.

    abstract fun taskDao(): TaskDao//Veritabanı üzerinden TaskDao'ya erişim sağlar.

    companion object {//Sınıfın dışından da erişilebilen, Singleton yapıyı kuran blok.

        @Volatile//INSTANCE değişkeni tüm thread'ler arasında senkronize olur. Çoklu iş parçacığı ortamında güvenli hale gelir.
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {// Veritabanı örneğini al veya oluştur
            return INSTANCE ?: synchronized(this) {//Eğer INSTANCE zaten varsa onu döndür.
                val instance = androidx.room.Room.databaseBuilder(//Room veritabanı oluşturulur.
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                )   .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance//Yoksa synchronized blok ile aynı anda sadece bir thread'in yeni veritabanı oluşturmasına izin verilir.
            }
        }
        // Migration from version 1 to 2 (yeni sütunlar eklendi)
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task_table ADD COLUMN mondayChecked INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE task_table ADD COLUMN tuesdayChecked INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE task_table ADD COLUMN wednesdayChecked INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE task_table ADD COLUMN thursdayChecked INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE task_table ADD COLUMN fridayChecked INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE task_table ADD COLUMN saturdayChecked INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE task_table ADD COLUMN sundayChecked INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}