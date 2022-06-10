package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.local.datastore.SettingPreferences
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
}