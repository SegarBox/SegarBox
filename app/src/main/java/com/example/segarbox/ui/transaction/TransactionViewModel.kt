package com.example.segarbox.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.RetrofitRepository
import com.example.core.data.source.remote.response.TransactionsResponse
import com.example.core.data.source.remote.response.UserCartResponse
import com.example.core.utils.Event
import kotlinx.coroutines.launch

class TransactionViewModel(private val retrofitRepository: RetrofitRepository): ViewModel() {
    private val _transactionsResponse = MutableLiveData<Event<TransactionsResponse>>()
    val transactionsResponse: LiveData<Event<TransactionsResponse>> = _transactionsResponse

    private var _userCart = MutableLiveData<Event<UserCartResponse>>()
    val userCart: LiveData<Event<UserCartResponse>> = _userCart

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getTransactions(token: String, status: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getTransactions(token, status)
            _transactionsResponse.postValue(Event(request))
            _isLoading.postValue(false)
        }
    }

    fun getUserCart(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getUserCart(token)
            _userCart.postValue(Event(request))
            _isLoading.postValue(false)
        }
    }
}