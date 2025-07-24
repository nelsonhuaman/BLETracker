package com.danp.bletracker.di

import android.content.Context
import androidx.room.Room
import com.danp.bletracker.data.local.AppDatabase
import com.danp.bletracker.data.local.ContactoCercanoDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "bletracker_db").build()

    @Provides
    fun provideContactoDao(db: AppDatabase): ContactoCercanoDao =
        db.contactoCercanoDao()
}