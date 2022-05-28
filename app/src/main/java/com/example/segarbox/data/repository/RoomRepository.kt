package com.example.segarbox.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.segarbox.data.local.database.CityDao
import com.example.segarbox.data.local.database.MainDatabase
import com.example.segarbox.data.local.database.ProductDao
import com.example.segarbox.data.remote.response.CityResults
import com.example.segarbox.data.remote.response.ProductItem

class RoomRepository(application: Application) {
    private val mCityDao: CityDao
    private val mProductDao: ProductDao

    init {
        val db = MainDatabase.getDatabase(application)
        mCityDao = db.cityDao()
        mProductDao = db.productDao()
    }

    suspend fun insertCity(cityResults: List<CityResults>) = mCityDao.insertCity(cityResults)

    fun getCityCount(): LiveData<Int> = mCityDao.getCityCount()

    fun getCity(city: String, type: String): LiveData<List<CityResults>> = mCityDao.getCity(city, type)

    suspend fun cobaInsertProduct(listProduct: List<ProductItem>) = mProductDao.insertProduct(listProduct)

}