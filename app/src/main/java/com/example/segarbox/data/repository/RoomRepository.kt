package com.example.segarbox.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.segarbox.data.local.database.CityDao
import com.example.segarbox.data.local.database.MainDatabase
import com.example.segarbox.data.remote.response.CityResults

class RoomRepository(application: Application) {
    private val mCityDao: CityDao

    init {
        val db = MainDatabase.getDatabase(application)
        mCityDao = db.cityDao()
    }

    suspend fun insertCity(cityResults: List<CityResults>) = mCityDao.insertCity(cityResults)

    fun getCityCount(): LiveData<Int> = mCityDao.getCityCount()

}