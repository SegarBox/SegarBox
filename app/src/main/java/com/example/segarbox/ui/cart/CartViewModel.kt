package com.example.segarbox.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.core.data.source.remote.response.CartDetailResponse
import com.example.segarbox.core.data.source.remote.response.DeleteCartResponse
import com.example.segarbox.core.data.source.remote.response.UpdateCartResponse
import com.example.segarbox.core.data.source.remote.response.UserCartResponse
import com.example.segarbox.core.data.RetrofitRepository
import kotlinx.coroutines.launch

class CartViewModel(private val retrofitRepository: RetrofitRepository): ViewModel() {

    private var _userCart = MutableLiveData<UserCartResponse>()
    val userCart: LiveData<UserCartResponse> = _userCart

    private var _isCheckedUserCart = MutableLiveData<UserCartResponse>()
    val isCheckedUserCart: LiveData<UserCartResponse> = _isCheckedUserCart

    private var _deleteUserCartResponse = MutableLiveData<DeleteCartResponse>()
    val deleteUserCartResponse: LiveData<DeleteCartResponse> = _deleteUserCartResponse

    private var _updateUserCartResponse = MutableLiveData<UpdateCartResponse>()
    val updateUserCartResponse: LiveData<UpdateCartResponse> = _updateUserCartResponse

    private var _cartDetailResponse = MutableLiveData<CartDetailResponse>()
    val cartDetailResponse: LiveData<CartDetailResponse> = _cartDetailResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserCart(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getUserCart(token)
            _userCart.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun getIsCheckedUserCart(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getIsCheckedUserCart(token)
            _isCheckedUserCart.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun deleteUserCart(token: String, cartId: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.deleteUserCart(token, cartId)
            _deleteUserCartResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun updateUserCart(token: String, cartId: Int, productId: Int, productQty: Int, isChecked: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.updateUserCart(token, cartId, productId, productQty, isChecked)
            _updateUserCartResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun getCartDetail(token: String, shippingCost: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getCartDetail(token, shippingCost)
            _cartDetailResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }
}