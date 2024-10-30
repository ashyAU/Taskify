package com.example.remindme.stopwatch

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
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
    val counterValue: Long = 0,
    val lastUpdatedTime: Long = 0,
    val isStarted: Boolean = false
)


@Dao
interface StopwatchDao {
    @Insert
    suspend fun insertLap(lap: Laps)

    // These are the lap queries
    @Query("DELETE FROM STOPWATCHLAPS")
    suspend fun deleteAllLaps()

    @Query("SELECT * FROM STOPWATCHLAPS")
    fun getLapsById(): Flow<List<Laps>>

    @Query("DELETE FROM sqlite_sequence WHERE name='stopwatchLaps'")
    suspend fun resetLapId()

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

    fun saveStopwatchValue(counterValue: Long, lastUpdatedTime: Long, isStarted: Boolean) {
        viewModelScope.launch {
            stopwatchDao.insertCounter(
                counterEntity = CounterEntity(
                    counterValue = counterValue,
                    lastUpdatedTime = lastUpdatedTime,
                    isStarted = isStarted
                )
            )
        }
    }

    fun getLastStopwatchValue(onResult: (CounterEntity?) -> Unit) {
        viewModelScope.launch {
            val lastValue = stopwatchDao.getCounter()
            onResult(lastValue)
        }
    }
}