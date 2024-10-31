package com.example.remindme

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

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
    suspend fun getTasksGroupById(): Flow<List<TasksGroup>>
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

        fun getDatabase(context: Context): Any {
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

/*@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTasksDatabase(@ApplicationContext context: Context): TasksDatabase {
        return TasksDatabase.getDatabase(context)
    }

}*/
