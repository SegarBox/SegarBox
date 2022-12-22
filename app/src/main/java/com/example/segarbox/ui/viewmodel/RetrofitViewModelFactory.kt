package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.data.Repository
import com.example.segarbox.ui.address.AddressViewModel
import com.example.segarbox.ui.cart.CartViewModel
import com.example.segarbox.ui.checkout.CheckoutViewModel
import com.example.segarbox.ui.detail.DetailViewModel
import com.example.segarbox.ui.invoice.InvoiceViewModel
import com.example.segarbox.ui.login.LoginViewModel
import com.example.segarbox.ui.login.RegisterViewModel
import com.example.segarbox.ui.maps.MapsViewModel
import com.example.segarbox.ui.pagination.PaginationViewModel
import com.example.segarbox.ui.profile.ProfileViewModel
import com.example.segarbox.ui.rating.RatingViewModel
import com.example.segarbox.ui.transaction.TransactionViewModel

class RetrofitViewModelFactory private constructor(private val retrofitRepository: Repository) : ViewModelProvider.NewInstanceFactory(){

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
            modelClass.isAssignableFrom(RatingViewModel::class.java) -> RatingViewModel(retrofitRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RetrofitViewModelFactory? = null

        @JvmStatic
        fun getInstance(retrofitRepository: Repository): RetrofitViewModelFactory {
            if (INSTANCE == null) {
                synchronized(RetrofitViewModelFactory::class.java) {
                    INSTANCE = RetrofitViewModelFactory(retrofitRepository)
                }
            }
            return INSTANCE as RetrofitViewModelFactory
        }
    }

}