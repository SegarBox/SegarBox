package com.example.segarbox.core.data.source.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.segarbox.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>){

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

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[keyToken] ?: ""
        }
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

    suspend fun logout() {
        dataStore.edit {
            it.remove(keyToken)
            it.remove(keyUserId)
        }
    }

    suspend fun saveBaseUrl(baseUrl: String) {
        dataStore.edit {
            it[keyBaseUrl] = baseUrl
        }
    }

    fun getBaseUrl(): Flow<String> {
        return dataStore.data.map {
            it[keyBaseUrl] ?: BuildConfig.BASE_URL_SEGARBOX
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}