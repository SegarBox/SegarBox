package com.example.segarbox.core.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.segarbox.core.data.source.local.room.CityDao
import com.example.segarbox.core.data.source.local.room.MainDatabase
import com.example.segarbox.core.data.source.remote.response.CityResults

class RoomRepository(application: Application) {
    private val mCityDao: CityDao

    init {
        val db = MainDatabase.getDatabase(application)
        mCityDao = db.cityDao()
    }

    suspend fun insertCity(cityResults: List<CityResults>) = mCityDao.insertCity(cityResults)

    fun getCityCount(): LiveData<Int> = mCityDao.getCityCount()

    fun getCity(city: String, type: String): LiveData<List<CityResults>> = mCityDao.getCity(city, type)

}
