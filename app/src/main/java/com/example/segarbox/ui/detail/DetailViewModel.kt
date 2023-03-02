package com.example.segarbox.ui.detail

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Product
import com.example.core.domain.usecase.DetailUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

}