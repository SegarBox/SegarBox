package com.example.core.data.source.local

import com.example.core.data.source.local.room.CityDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val cityDao: CityDao){

}