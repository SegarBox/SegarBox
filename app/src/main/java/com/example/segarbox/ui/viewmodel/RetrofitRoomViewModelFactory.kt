package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.data.repository.RoomRepository

class RetrofitRoomViewModelFactory private constructor(
    private val roomRepository: RoomRepository,
    private val retrofitRepository: RetrofitRepository,
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(roomRepository, retrofitRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RetrofitRoomViewModelFactory? = null

        @JvmStatic
        fun getInstance(roomRepository: RoomRepository, retrofitRepository: RetrofitRepository): RetrofitRoomViewModelFactory {
            if (INSTANCE == null) {
                synchronized(PrefViewModelFactory::class.java) {
                    INSTANCE = RetrofitRoomViewModelFactory(roomRepository, retrofitRepository)
                }
            }
            return INSTANCE as RetrofitRoomViewModelFactory
        }
    }

}