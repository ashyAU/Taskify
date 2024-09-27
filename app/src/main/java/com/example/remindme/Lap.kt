package com.example.remindme

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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


@Entity
data class Lap(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: String
)

@Dao
interface LapDao {
    @Insert
    suspend fun insertLap(lap: Lap)


    @Query("DELETE FROM Lap") // Add this method to delete all laps
    suspend fun deleteAllLaps()

    @Query("SELECT * FROM Lap")
    fun getLapsById(): Flow<List<Lap>>

    @Query("DELETE FROM sqlite_sequence WHERE name='Lap'") // Resets the id counter
    suspend fun resetLapId()
}

@Database(
    entities = [Lap::class],
    version = 1
)
abstract class LapDatabase : RoomDatabase() {
    abstract fun dao(): LapDao

    companion object {
        @Volatile
        private var INSTANCE: LapDatabase? = null

        fun getDatabase(context: Context): LapDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LapDatabase::class.java,
                    "lap_database"
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
    fun provideLapDatabase(@ApplicationContext context: Context): LapDatabase {
        return LapDatabase.getDatabase(context)
    }

    @Provides
    fun provideLapDao(database: LapDatabase): LapDao {
        return database.dao()
    }
}

@HiltViewModel
class LapViewModel @Inject constructor(private val lapDao: LapDao) : ViewModel() {
    val allLaps: Flow<List<Lap>> = lapDao.getLapsById()

    fun addLap(time: String) {
        viewModelScope.launch {
            lapDao.insertLap(Lap(time = time))
        }
    }

    fun deleteAllLaps() {
        viewModelScope.launch {
            lapDao.deleteAllLaps()
            lapDao.resetLapId()
        }
    }
}


