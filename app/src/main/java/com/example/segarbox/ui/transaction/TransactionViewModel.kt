package com.example.segarbox.ui.transaction

import androidx.lifecycle.*
import com.example.core.data.Repository
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.TransactionsResponse
import com.example.core.data.source.remote.response.UserCartResponse
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Transaction
import com.example.core.domain.usecase.TransactionUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val transactionUseCase: TransactionUseCase): ViewModel() {

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getCart(token: String): LiveData<Event<Resource<List<Cart>>>> =
        transactionUseCase.getCart(token).asLiveData().map {
            Event(it)
        }

    fun getTransactions(token: String, status: String): LiveData<Event<Resource<List<Transaction>>>> =
        transactionUseCase.getTransactions(token, status).asLiveData().map {
            Event(it)
        }

    fun getToken(): LiveData<Event<String>> =
        transactionUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

//    private val _transactionsResponse = MutableLiveData<Event<TransactionsResponse>>()
//    val transactionsResponse: LiveData<Event<TransactionsResponse>> = _transactionsResponse
//
//    private var _userCart = MutableLiveData<Event<UserCartResponse>>()
//    val userCart: LiveData<Event<UserCartResponse>> = _userCart
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun getTransactions(token: String, status: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getTransactions(token, status)
//            _transactionsResponse.postValue(Event(request))
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun getUserCart(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getUserCart(token)
//            _userCart.postValue(Event(request))
//            _isLoading.postValue(false)
//        }
//    }
}