package com.example.remindme

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow


@Entity
data class Lap(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val time: String
)

@Dao
interface LapDao {
    @Insert
    suspend fun insertLap(lap: Lap)

    @Delete
    suspend fun deleteLaps(lap: Lap)

    @Query("SELECT * FROM Lap")
    fun getLapsById(): Flow<List<Lap>>
}


@Database(
    entities = [Lap::class],
    version = 1
)
abstract class LapDatabase: RoomDatabase()
{
    abstract fun dao(): LapDao
}