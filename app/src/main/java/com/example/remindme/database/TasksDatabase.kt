package com.example.remindme.database

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Entity(tableName = "taskGroup")
data class TasksGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val groupName: String
)

@Dao
interface TasksDao {
    // This will create a parent for the tasks
    @Insert
    suspend fun insertTaskGroup(tasksGroup: TasksGroup)

    @Query("SELECT * FROM TASKGROUP")
    fun getTasksGroupById(): Flow<List<TasksGroup>>

    @Query("DELETE FROM TASKGROUP WHERE groupName = :groupName")
    suspend fun deleteTaskGroup(groupName: String)

    @Query("UPDATE TASKGROUP SET groupName = :newGroupName WHERE id = :id")
    suspend fun updateTaskGroupName(id: Int, newGroupName: String)
}


@Database(
    entities = [TasksGroup::class],
    version = 1
)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun dao(): TasksDao

    companion object {
        @Volatile
        private var INSTANCE: TasksDatabase? = null

        fun getDatabase(context: Context): TasksDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TasksDatabase::class.java,
                    "tasks_database"
                        ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@HiltViewModel
class TasksViewModel @Inject constructor(private val tasksDao: TasksDao): ViewModel() {
    val allTasks: Flow<List<TasksGroup>> = tasksDao.getTasksGroupById()

    fun addTaskGroup(groupName: String) {
        viewModelScope.launch {
            tasksDao.insertTaskGroup(TasksGroup(groupName = groupName))
        }
    }
    fun deleteTaskGroup(groupName: String) {
        viewModelScope.launch {
            tasksDao.deleteTaskGroup(groupName = groupName)
        }
    }
    fun updateGroupName(id: Int, newGroupName: String)
    {
        viewModelScope.launch {
            tasksDao.updateTaskGroupName(id, newGroupName)
        }
    }

}
