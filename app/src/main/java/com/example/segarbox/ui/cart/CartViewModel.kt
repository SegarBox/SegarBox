package com.example.segarbox.ui.cart

import android.util.Log
import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartDetail
import com.example.core.domain.usecase.CartUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    fun getCart(token: String) = viewModelScope.launch(Dispatchers.IO) {
        cartUseCase.getCart(token).collect {
            _getCartResponse.postValue(Event(it))
        }
    }

    fun getCartDetail(token: String, shippingCost: Int) = viewModelScope.launch(Dispatchers.IO) {
        cartUseCase.getCartDetail(token, shippingCost).collect {
            _getCartDetailResponse.postValue(Event(it))
        }
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
    ) = viewModelScope.launch(Dispatchers.IO) {
        cartUseCase.updateCart(token, cartId, productId, productQty, isChecked).collect {
            _updateCartResponse.postValue(Event(it))
        }
    }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

}