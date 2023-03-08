package com.example.segarbox.ui.checkout

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.model.*
import com.example.core.domain.usecase.CheckoutUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val checkoutUseCase: CheckoutUseCase) : ViewModel() {

    private val _getTokenResponse = MutableLiveData<Event<String>>()
    val getTokenResponse: LiveData<Event<String>> = _getTokenResponse

    private val _getUserResponse = MutableLiveData<Event<Resource<User>>>()
    val getUserResponse: LiveData<Event<Resource<User>>> = _getUserResponse

    private val _getCheckedCartResponse = MutableLiveData<Event<Resource<List<Cart>>>>()
    val getCheckedCartResponse: LiveData<Event<Resource<List<Cart>>>> = _getCheckedCartResponse

    private val _getCartDetailResponse = MutableLiveData<Event<Resource<CartDetail>>>()
    val getCartDetailResponse: LiveData<Event<Resource<CartDetail>>> = _getCartDetailResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    init {
        getToken()
    }

    fun getToken() = viewModelScope.launch(Dispatchers.IO) {
        checkoutUseCase.getToken().collect {
            _getTokenResponse.postValue(Event(it))
        }
    }

    fun getUser(token: String) = viewModelScope.launch(Dispatchers.IO) {
        checkoutUseCase.getUser(token).collect {
            _getUserResponse.postValue(Event(it))
        }
    }

    fun getCheckedCart(token: String) = viewModelScope.launch(Dispatchers.IO) {
        checkoutUseCase.getCheckedCart(token).collect {
            _getCheckedCartResponse.postValue(Event(it))
        }
    }

    fun getCartDetail(token: String, shippingCost: Int) = viewModelScope.launch(Dispatchers.IO) {
        checkoutUseCase.getCartDetail(token, shippingCost).collect {
            _getCartDetailResponse.postValue(Event(it))
        }
    }

    fun makeOrder(token: String, makeOrderBody: MakeOrderBody): LiveData<Event<Resource<MakeOrder>>> =
        checkoutUseCase.makeOrder(token, makeOrderBody).asLiveData().map {
            Event(it)
        }

    fun getTransactionById(token: String, transactionId: Int): LiveData<Event<Resource<Transaction>>> =
        checkoutUseCase.getTransactionById(token, transactionId).asLiveData().map {
            Event(it)
        }


    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

}