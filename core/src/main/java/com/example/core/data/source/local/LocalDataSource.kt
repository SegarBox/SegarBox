package com.example.core.data.source.local

import com.example.core.data.Resource
import com.example.core.data.source.local.room.CityDao
import com.example.core.domain.model.City
import com.example.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val cityDao: CityDao) {

    fun getCity(city: String, type: String): Flow<Resource<List<City>>> = flow {
        emit(Resource.Loading())
        try {
            val data = cityDao.getCity(city, type)
            data.map { results ->
                if (results.isNotEmpty()) {
                    emit(Resource.Success(DataMapper.mapCityResultsToCities(results)))
                } else {
                    emit(Resource.Empty())
                }
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access cities"))
        }
    }

    fun getCityCount(): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            val data = cityDao.getCityCount()
            data.map {
                emit(Resource.Success(it))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't count cities"))
        }
    }

    suspend fun insertCityToDb(listCity: List<City>) =
        cityDao.insertCity(DataMapper.mapCitiesToCityResults(listCity))

}