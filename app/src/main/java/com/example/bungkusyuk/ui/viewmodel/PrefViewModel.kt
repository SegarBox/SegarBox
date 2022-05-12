package com.example.bungkusyuk.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bungkusyuk.data.local.datastore.SettingPreferences
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

}