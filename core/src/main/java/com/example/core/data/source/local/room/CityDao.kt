package com.example.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.source.remote.response.CityResults
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCity(cityResults: List<CityResults>)

    @Query("SELECT COUNT(*) FROM city")
    fun getCityCount(): Flow<Int>

    @Query("SELECT * FROM city WHERE cityName = :city AND type = :type")
    fun getCity(city: String, type: String): Flow<List<CityResults>>
}