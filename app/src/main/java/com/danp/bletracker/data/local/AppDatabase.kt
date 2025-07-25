package com.danp.bletracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [ContactoCercanoEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactoCercanoDao(): ContactoCercanoDao
}