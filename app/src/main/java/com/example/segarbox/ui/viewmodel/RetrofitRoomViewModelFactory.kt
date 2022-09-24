package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.segarbox.core.data.RetrofitRepository
import com.example.segarbox.core.data.RoomRepository

class RetrofitRoomViewModelFactory private constructor(
    private val roomRepository: RoomRepository,
    private val retrofitRepository: RetrofitRepository,
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(roomRepository, retrofitRepository) as T
            modelClass.isAssignableFrom(ShippingViewModel::class.java) -> ShippingViewModel(roomRepository, retrofitRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RetrofitRoomViewModelFactory? = null

        @JvmStatic
        fun getInstance(roomRepository: RoomRepository, retrofitRepository: RetrofitRepository): RetrofitRoomViewModelFactory {
            if (INSTANCE == null) {
                synchronized(RetrofitRoomViewModelFactory::class.java) {
                    INSTANCE = RetrofitRoomViewModelFactory(roomRepository, retrofitRepository)
                }
            }
            return INSTANCE as RetrofitRoomViewModelFactory
        }
    }

}