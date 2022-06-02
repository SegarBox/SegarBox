package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.local.model.MakeOrderBody
import com.example.segarbox.data.local.model.ProductTransactions
import com.example.segarbox.data.remote.response.CartDetailResponse
import com.example.segarbox.data.remote.response.MakeOrderResponse
import com.example.segarbox.data.remote.response.UserCartResponse
import com.example.segarbox.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class CheckoutViewModel(private val retrofitRepository: RetrofitRepository) : ViewModel() {

    private val _checkoutDetails = MutableLiveData<UserCartResponse>()
    val checkoutDetails: LiveData<UserCartResponse> = _checkoutDetails

    private val _costs = MutableLiveData<CartDetailResponse>()
    val costs: LiveData<CartDetailResponse> = _costs

    private val _makeOrderResponse = MutableLiveData<MakeOrderResponse>()
    val makeOrderResponse: LiveData<MakeOrderResponse> = _makeOrderResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCheckoutDetails(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getIsCheckedUserCart(token)
            _checkoutDetails.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun getCosts(token: String, shippingCost: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getCartDetail(token, shippingCost)
            _costs.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun makeOrderTransaction(token: String, makeOrderBody: MakeOrderBody) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.makeOrderTransaction(token, makeOrderBody)
            _makeOrderResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }
}