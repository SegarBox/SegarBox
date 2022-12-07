package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.local.datastore.SettingPreferences
import com.example.core.utils.Event
import kotlinx.coroutines.launch

class PrefViewModel(private val pref: SettingPreferences): ViewModel() {

    fun getTheme(): LiveData<Boolean> {
        return pref.getTheme().asLiveData()
    }

    fun saveTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            pref.saveTheme(isDarkMode)
        }
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun getUserId(): Event<LiveData<Int>> {
        return Event(pref.getUserId().asLiveData())
    }

    fun saveUserId(userId: Int) {
        viewModelScope.launch {
            pref.saveUserId(userId)
        }
    }

    fun getIntro(): LiveData<Boolean> {
        return pref.getIntro().asLiveData()
    }

    fun saveIntro(isAlreadyIntro: Boolean) {
        viewModelScope.launch {
            pref.saveIntro(isAlreadyIntro)
        }
    }

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun saveBaseUrl(baseUrl: String) {
        viewModelScope.launch {
            pref.saveBaseUrl(baseUrl)
        }
    }

    fun getBaseUrl(): LiveData<String> {
        return pref.getBaseUrl().asLiveData()
    }


}