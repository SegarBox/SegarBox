package com.example.segarbox.ui.detail

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Product
import com.example.core.domain.usecase.DetailUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val detailUseCase: DetailUseCase): ViewModel() {

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _getProductByIdResponse = MutableLiveData<Event<Resource<Product>>>()
    val getProductByIdResponse: LiveData<Event<Resource<Product>>> = _getProductByIdResponse

    private val _getCartResponse = MutableLiveData<Event<Resource<List<Cart>>>>()
    val getCartResponse: LiveData<Event<Resource<List<Cart>>>> = _getCartResponse

    init {
        saveQuantity(0)
    }

    fun saveQuantity(qty: Int) {
        _quantity.postValue(qty)
    }

    fun getToken(): LiveData<Event<String>> =
        detailUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun getProductById(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        detailUseCase.getProductById(id).collect {
            _getProductByIdResponse.postValue(Event(it))
        }
    }

    fun getCart(token: String) = viewModelScope.launch(Dispatchers.IO) {
        detailUseCase.getCart(token).collect {
            _getCartResponse.postValue(Event(it))
        }
    }

    fun addCart(token: String, productId: Int, productQty: Int): LiveData<Event<Resource<String>>> =
        detailUseCase.addCart(token, productId, productQty).asLiveData().map {
            Event(it)
        }

    fun updateCart(
        token: String,
        cartId: Int,
        productId: Int,
        productQty: Int,
        isChecked: Int,
    ): LiveData<Event<Resource<String>>> =
        detailUseCase.updateCart(token, cartId, productId, productQty, isChecked).asLiveData().map {
            Event(it)
        }

    fun deleteCart(token: String, cartId: Int): LiveData<Event<Resource<String>>> =
        detailUseCase.deleteCart(token, cartId).asLiveData().map {
            Event(it)
        }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

//    private var _productById = MutableLiveData<ProductByIdResponse>()
//    val productById: LiveData<ProductByIdResponse> = _productById
//
//    private var _userCart = MutableLiveData<UserCartResponse>()
//    val userCart: LiveData<UserCartResponse> = _userCart
//
//    private var _addCartResponse = MutableLiveData<AddCartResponse>()
//    val addCartResponse: LiveData<AddCartResponse> = _addCartResponse
//
//    private var _updateUserCartResponse = MutableLiveData<UpdateCartResponse>()
//    val updateUserCartResponse: LiveData<UpdateCartResponse> = _updateUserCartResponse
//
//    private var _deleteUserCartResponse = MutableLiveData<DeleteCartResponse>()
//    val deleteUserCartResponse: LiveData<DeleteCartResponse> = _deleteUserCartResponse
//
//    private var _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading



//    fun getProductById(id: Int) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getProductById(id)
//            _productById.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun getUserCart(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getUserCart(token)
//            _userCart.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun addCart(token: String, productId: Int, productQty: Int) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.addCart(token, productId, productQty)
//            _addCartResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun updateUserCart(token: String, cartId: Int, productId: Int, productQty: Int, isChecked: Int) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.updateUserCart(token, cartId, productId, productQty, isChecked)
//            _updateUserCartResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun deleteUserCart(token: String, cartId: Int) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.deleteUserCart(token, cartId)
//            _deleteUserCartResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
}