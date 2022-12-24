package com.example.segarbox.ui.cart

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartDetail
import com.example.core.domain.usecase.CartUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartUseCase: CartUseCase) : ViewModel() {

    private val _getCartResponse = MutableLiveData<Event<Resource<List<Cart>>>>()
    val getCartResponse: LiveData<Event<Resource<List<Cart>>>> = _getCartResponse

    private val _getCartDetailResponse = MutableLiveData<Event<Resource<CartDetail>>>()
    val getCartDetailResponse: LiveData<Event<Resource<CartDetail>>> = _getCartDetailResponse

    private val _updateCartResponse = MutableLiveData<Event<Resource<String>>>()
    val updateCartResponse: LiveData<Event<Resource<String>>> = _updateCartResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getToken(): LiveData<Event<String>> =
        cartUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun getCart(token: String) =
        cartUseCase.getCart(token).asLiveData().map {
            _getCartResponse.postValue(Event(it))
        }

    fun getCartDetail(token: String, shippingCost: Int) =
        cartUseCase.getCartDetail(token, shippingCost).asLiveData().map {
            _getCartDetailResponse.postValue(Event(it))
        }

    fun getCheckedCart(token: String): LiveData<Event<Resource<List<Cart>>>> =
        cartUseCase.getCheckedCart(token).asLiveData().map {
            Event(it)
        }

    fun deleteCart(token: String, cartId: Int): LiveData<Event<Resource<String>>> =
        cartUseCase.deleteCart(token, cartId).asLiveData().map {
            Event(it)
        }

    fun updateCart(
        token: String,
        cartId: Int,
        productId: Int,
        productQty: Int,
        isChecked: Int,
    ) = cartUseCase.updateCart(token, cartId, productId, productQty, isChecked).asLiveData().map {
            _updateCartResponse.postValue(Event(it))
        }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

//    private var _userCart = MutableLiveData<UserCartResponse>()
//    val userCart: LiveData<UserCartResponse> = _userCart
//
//    private var _isCheckedUserCart = MutableLiveData<UserCartResponse>()
//    val isCheckedUserCart: LiveData<UserCartResponse> = _isCheckedUserCart
//
//    private var _deleteUserCartResponse = MutableLiveData<DeleteCartResponse>()
//    val deleteUserCartResponse: LiveData<DeleteCartResponse> = _deleteUserCartResponse
//
//    private var _updateUserCartResponse = MutableLiveData<UpdateCartResponse>()
//    val updateUserCartResponse: LiveData<UpdateCartResponse> = _updateUserCartResponse
//
//    private var _cartDetailResponse = MutableLiveData<CartDetailResponse>()
//    val cartDetailResponse: LiveData<CartDetailResponse> = _cartDetailResponse
//
//    private var _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
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
//    fun getIsCheckedUserCart(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getIsCheckedUserCart(token)
//            _isCheckedUserCart.postValue(request)
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
//    fun getCartDetail(token: String, shippingCost: Int) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getCartDetail(token, shippingCost)
//            _cartDetailResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
}