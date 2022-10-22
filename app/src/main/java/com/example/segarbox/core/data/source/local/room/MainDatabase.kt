package com.example.segarbox.core.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.segarbox.core.data.source.remote.response.CityResults

@Database(entities = [CityResults::class], version = 7, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): MainDatabase {
            if (INSTANCE == null) {
                synchronized(MainDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        "main_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE as MainDatabase
        }
    }
}