package com.example.remindme.database

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TasksDatabase {
        return TasksDatabase.getDatabase(context)
    }
    @Provides
    fun provideTasksDao(database: TasksDatabase): TasksDao {
        return database.dao()
    }

}