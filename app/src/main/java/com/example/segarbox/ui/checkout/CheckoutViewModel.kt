package com.example.segarbox.ui.checkout

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartDetail
import com.example.core.domain.model.MakeOrder
import com.example.core.domain.usecase.CheckoutUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val checkoutUseCase: CheckoutUseCase) : ViewModel() {

    private val _getCheckedCartResponse = MutableLiveData<Event<Resource<List<Cart>>>>()
    val getCheckedCartResponse: LiveData<Event<Resource<List<Cart>>>> = _getCheckedCartResponse

    private val _getCartDetailResponse = MutableLiveData<Event<Resource<CartDetail>>>()
    val getCartDetailResponse: LiveData<Event<Resource<CartDetail>>> = _getCartDetailResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getToken(): LiveData<Event<String>> =
        checkoutUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun getCheckedCart(token: String) =
        checkoutUseCase.getCheckedCart(token).asLiveData().map {
            _getCheckedCartResponse.postValue(Event(it))
        }

    fun getCartDetail(token: String, shippingCost: Int) =
        checkoutUseCase.getCartDetail(token, shippingCost).asLiveData().map {
            _getCartDetailResponse.postValue(Event(it))
        }

    fun makeOrder(token: String, makeOrderBody: MakeOrderBody): LiveData<Event<Resource<MakeOrder>>> =
        checkoutUseCase.makeOrder(token, makeOrderBody).asLiveData().map {
            Event(it)
        }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

//    private val _checkoutDetails = MutableLiveData<UserCartResponse>()
//    val checkoutDetails: LiveData<UserCartResponse> = _checkoutDetails
//
//    private val _costs = MutableLiveData<CartDetailResponse>()
//    val costs: LiveData<CartDetailResponse> = _costs
//
//    private val _makeOrderResponse = MutableLiveData<MakeOrderResponse>()
//    val makeOrderResponse: LiveData<MakeOrderResponse> = _makeOrderResponse
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun getCheckoutDetails(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getIsCheckedUserCart(token)
//            _checkoutDetails.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun getCosts(token: String, shippingCost: Int) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getCartDetail(token, shippingCost)
//            _costs.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun makeOrderTransaction(token: String, makeOrderBody: MakeOrderBody) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.makeOrderTransaction(token, makeOrderBody)
//            _makeOrderResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
}