package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.data.source.local.datastore.SettingPreferences

class PrefViewModelFactory(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PrefViewModel::class.java) -> PrefViewModel(pref) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
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