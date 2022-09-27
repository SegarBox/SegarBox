package com.example.segarbox.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.core.data.source.remote.response.*
import com.example.segarbox.core.data.RetrofitRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val retrofitRepository: RetrofitRepository): ViewModel() {

    private var _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private var _productById = MutableLiveData<ProductByIdResponse>()
    val productById: LiveData<ProductByIdResponse> = _productById

    private var _userCart = MutableLiveData<UserCartResponse>()
    val userCart: LiveData<UserCartResponse> = _userCart

    private var _addCartResponse = MutableLiveData<AddCartResponse>()
    val addCartResponse: LiveData<AddCartResponse> = _addCartResponse

    private var _updateUserCartResponse = MutableLiveData<UpdateCartResponse>()
    val updateUserCartResponse: LiveData<UpdateCartResponse> = _updateUserCartResponse

    private var _deleteUserCartResponse = MutableLiveData<DeleteCartResponse>()
    val deleteUserCartResponse: LiveData<DeleteCartResponse> = _deleteUserCartResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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

    fun getUserCart(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getUserCart(token)
            _userCart.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun addCart(token: String, productId: Int, productQty: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.addCart(token, productId, productQty)
            _addCartResponse.postValue(request)
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

    fun deleteUserCart(token: String, cartId: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.deleteUserCart(token, cartId)
            _deleteUserCartResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }
}