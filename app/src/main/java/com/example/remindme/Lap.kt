package com.example.remindme

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Entity(tableName = "stopwatchLaps")
data class Laps(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: String
)
@Entity(tableName = "counter")
data class CounterEntity(
    @PrimaryKey val id: Int = 0,
    val counterValue: Int,
    val lastUpdatedTime: Long
)


@Dao
interface StopwatchDao {
    @Insert
    suspend fun insertLap(lap: Laps)

    // These are the lap queries
    @Query("DELETE FROM STOPWATCHLAPS") // Add this method to delete all laps
    suspend fun deleteAllLaps()

    @Query("SELECT * FROM STOPWATCHLAPS")
    fun getLapsById(): Flow<List<Laps>>

    @Query("DELETE FROM sqlite_sequence WHERE name='stopwatchLaps'") // Resets the id counter
    suspend fun resetLapId()

    // These are the counter queries.
    @Query("SELECT * FROM counter WHERE id = 0 LIMIT 1")
    suspend fun getCounter(): CounterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCounter(counterEntity: CounterEntity)

}

@Database(
    entities = [Laps::class, CounterEntity::class],
    version = 1
)
abstract class StopwatchDatabase : RoomDatabase() {
    abstract fun dao(): StopwatchDao

    companion object {
        @Volatile
        private var INSTANCE: StopwatchDatabase? = null

        fun getDatabase(context: Context): StopwatchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StopwatchDatabase::class.java,
                    "stopwatch_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLapDatabase(@ApplicationContext context: Context): StopwatchDatabase {
        return StopwatchDatabase.getDatabase(context)
    }

    @Provides
    fun provideLapDao(database: StopwatchDatabase): StopwatchDao {
        return database.dao()
    }

}

@HiltViewModel
class StopwatchViewModel @Inject constructor(private val stopwatchDao: StopwatchDao) : ViewModel() {
    val allLaps: Flow<List<Laps>> = stopwatchDao.getLapsById()

    var counter by mutableIntStateOf(0)

    fun addLap(time: String) {
        viewModelScope.launch {
            stopwatchDao.insertLap(Laps(time = time))
        }
    }

    fun deleteAllLaps() {
        viewModelScope.launch {
            stopwatchDao.deleteAllLaps()
            stopwatchDao.resetLapId()
        }
    }
}


