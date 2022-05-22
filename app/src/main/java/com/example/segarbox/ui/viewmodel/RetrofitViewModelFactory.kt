package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.segarbox.data.repository.RetrofitRepository

class RetrofitViewModelFactory private constructor(private val repo: RetrofitRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> MapsViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RetrofitViewModelFactory? = null

        @JvmStatic
        fun getInstance(repo: RetrofitRepository): RetrofitViewModelFactory {
            if (INSTANCE == null) {
                synchronized(PrefViewModelFactory::class.java) {
                    INSTANCE = RetrofitViewModelFactory(repo)
                }
            }
            return INSTANCE as RetrofitViewModelFactory
        }
    }

}