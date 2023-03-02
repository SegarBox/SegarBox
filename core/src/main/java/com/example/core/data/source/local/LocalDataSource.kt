package com.example.core.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.core.data.Resource
import com.example.core.data.source.local.room.CityDao
import com.example.core.domain.model.City
import com.example.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val cityDao: CityDao,
    private val dataStore: DataStore<Preferences>,
) {
    private val keyTheme = booleanPreferencesKey("key_theme")
    private val keyIntro = booleanPreferencesKey("key_intro")
    private val keyToken = stringPreferencesKey("key_token")
    private val keyUserId = intPreferencesKey("key_userid")
    private val keyBaseUrl = stringPreferencesKey("key_base_url")

    fun getTheme(): Flow<Boolean> {
        return dataStore.data.map {
            it[keyTheme] ?: false
        }
    }

    suspend fun saveTheme(isDarkMode: Boolean) {
        dataStore.edit {
            it[keyTheme] = isDarkMode
        }
    }

    fun getToken(): Flow<String> =
        dataStore.data.map { pref ->
            pref[keyToken] ?: ""
        }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[keyToken] = token
        }
    }

    fun getUserId(): Flow<Int> {
        return dataStore.data.map {
            it[keyUserId] ?: 0
        }
    }

    suspend fun saveUserId(userId: Int) {
        dataStore.edit {
            it[keyUserId] = userId
        }
    }

    fun getIntro(): Flow<Boolean> {
        return dataStore.data.map {
            it[keyIntro] ?: false
        }
    }

    suspend fun saveIntro(isAlreadyIntro: Boolean) {
        dataStore.edit {
            it[keyIntro] = isAlreadyIntro
        }
    }

    suspend fun deleteToken() {
        dataStore.edit {
            it.remove(keyToken)
            it.remove(keyUserId)
        }
    }

    fun getCity(city: String, type: String): Flow<Resource<List<City>>> =
        cityDao.getCity(city, type).map {
            Resource.Success(DataMapper.mapCityResultsToCities(it))
        }

    fun getCityCount(): Flow<Resource<Int>> =
        cityDao.getCityCount().map {
            Resource.Success(it)
        }

    suspend fun insertCityToDb(listCity: List<City>) =
        cityDao.insertCity(DataMapper.mapCitiesToCityResults(listCity))

}