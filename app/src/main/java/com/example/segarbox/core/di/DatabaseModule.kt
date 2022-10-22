package com.example.segarbox.core.di

import android.content.Context
import androidx.room.Room
import com.example.segarbox.core.data.source.local.room.CityDao
import com.example.segarbox.core.data.source.local.room.MainDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MainDatabase {
        return Room.databaseBuilder(
            context,
            MainDatabase::class.java, "main_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCityDao(mainDatabase: MainDatabase): CityDao = mainDatabase.cityDao()

}