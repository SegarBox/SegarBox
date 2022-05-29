package com.example.segarbox.data.local.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.segarbox.data.remote.response.UserLogin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val keyTheme = booleanPreferencesKey("key_theme")
    private val keyToken = stringPreferencesKey("key_token")
    private val keyUserId = intPreferencesKey("key_user_id")


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
            Log.e("eeee get", "${it[keyUserId] ?: 0}")
        }
    }

    suspend fun saveUserId(id: Int) {
        dataStore.edit {
            it[keyUserId] = id
            Log.e("eeee save", it[keyUserId].toString())
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