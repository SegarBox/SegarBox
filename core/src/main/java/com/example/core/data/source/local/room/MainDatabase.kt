package com.example.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.source.remote.response.CityResults

@Database(entities = [CityResults::class], version = 8, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}