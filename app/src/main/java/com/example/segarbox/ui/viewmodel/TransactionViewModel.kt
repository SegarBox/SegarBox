package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.remote.response.DeleteAddressResponse
import com.example.segarbox.data.remote.response.TransactionsResponse
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.helper.Event
import kotlinx.coroutines.launch

class TransactionViewModel(private val retrofitRepository: RetrofitRepository): ViewModel() {
    private val _transactionsResponse = MutableLiveData<Event<TransactionsResponse>>()
    val transactionsResponse: LiveData<Event<TransactionsResponse>> = _transactionsResponse

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
}