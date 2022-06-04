package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.segarbox.data.repository.RetrofitRepository

class RetrofitViewModelFactory private constructor(private val retrofitRepository: RetrofitRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> MapsViewModel(retrofitRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(retrofitRepository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(retrofitRepository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(retrofitRepository) as T
            modelClass.isAssignableFrom(PaginationViewModel::class.java) -> PaginationViewModel(retrofitRepository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(retrofitRepository) as T
            modelClass.isAssignableFrom(CartViewModel::class.java) -> CartViewModel(retrofitRepository) as T
            modelClass.isAssignableFrom(CheckoutViewModel::class.java) -> CheckoutViewModel(retrofitRepository) as T
            modelClass.isAssignableFrom(AddressViewModel::class.java) -> AddressViewModel(retrofitRepository) as T
            modelClass.isAssignableFrom(InvoiceViewModel::class.java) -> InvoiceViewModel(retrofitRepository) as T
            modelClass.isAssignableFrom(TransactionViewModel::class.java) -> TransactionViewModel(retrofitRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RetrofitViewModelFactory? = null

        @JvmStatic
        fun getInstance(retrofitRepository: RetrofitRepository): RetrofitViewModelFactory {
            if (INSTANCE == null) {
                synchronized(PrefViewModelFactory::class.java) {
                    INSTANCE = RetrofitViewModelFactory(retrofitRepository)
                }
            }
            return INSTANCE as RetrofitViewModelFactory
        }
    }

}