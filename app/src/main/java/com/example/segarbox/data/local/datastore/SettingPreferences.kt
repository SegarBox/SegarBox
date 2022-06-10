package com.example.segarbox.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val keyTheme = booleanPreferencesKey("key_theme")
    private val keyIntro = booleanPreferencesKey("key_intro")
    private val keyToken = stringPreferencesKey("key_token")

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