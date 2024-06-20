package com.chhorvorn.material

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Database(entities = [TASK_ITEM::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): itemDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Entity
data class TASK_ITEM(
    @PrimaryKey (autoGenerate = true) val uid: Int,
    @ColumnInfo (name = "title") val title: String?,
    @ColumnInfo (name = "desc") val desc: String?,
    @ColumnInfo (name = "status") val status: Boolean
)

@Dao
interface itemDao {
    @Query("SELECT * FROM TASK_ITEM")
    fun getAll(): List<TASK_ITEM>
    @Query("SELECT * FROM TASK_ITEM WHERE status = 1")
    fun getDataWithStatusTrue(): List<TASK_ITEM>
    @Insert
    fun insert(taskItem: TASK_ITEM)

    @Delete
    fun delete(taskItem: TASK_ITEM)

    @Update
    fun update(taskItem: TASK_ITEM)
}