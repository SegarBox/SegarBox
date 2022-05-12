package com.example.bungkusyuk.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bungkusyuk.data.local.datastore.SettingPreferences

class PrefViewModelFactory private constructor(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrefViewModel::class.java)) {
            return PrefViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: PrefViewModelFactory? = null

        @JvmStatic
        fun getInstance(pref: SettingPreferences): PrefViewModelFactory {
            if (INSTANCE == null) {
                synchronized(PrefViewModelFactory::class.java) {
                    INSTANCE = PrefViewModelFactory(pref)
                }
            }
            return INSTANCE as PrefViewModelFactory
        }
    }

}