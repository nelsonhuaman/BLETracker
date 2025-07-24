package com.danp.bletracker.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {

    @Provides
    @Singleton
    fun provideWorkManagerConfiguration(
        @ApplicationContext context: Context,
        workerFactory: HiltWorkerFactory
    ): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}