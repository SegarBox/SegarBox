package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.remote.response.AddCartResponse
import com.example.segarbox.data.remote.response.ProductByIdResponse
import com.example.segarbox.data.remote.response.ProductResponse
import com.example.segarbox.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val retrofitRepository: RetrofitRepository): ViewModel() {

    private var _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private var _productById = MutableLiveData<ProductByIdResponse>()
    val productById: LiveData<ProductByIdResponse> = _productById

    private var _addCartResponse = MutableLiveData<AddCartResponse>()
    val addCartResponse: LiveData<AddCartResponse> = _addCartResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _goToCart = MutableLiveData<Boolean>()
    val goToCart: LiveData<Boolean> = _goToCart

    init {
        saveQuantity(0)
    }

    fun saveQuantity(qty: Int) {
        _quantity.postValue(qty)
    }

    fun getProductById(id: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getProductById(id)
            _productById.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun addCart(token: String, productId: Int, productQty: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _goToCart.postValue(false)
            val request = retrofitRepository.addCart(token, productId, productQty)
            _addCartResponse.postValue(request)
            _isLoading.postValue(false)
            _goToCart.postValue(true)
        }
    }
}