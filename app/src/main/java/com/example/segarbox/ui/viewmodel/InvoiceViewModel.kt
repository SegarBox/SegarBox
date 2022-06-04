package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.remote.response.TransactionByIdResponse
import com.example.segarbox.data.remote.response.UserResponse
import com.example.segarbox.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class InvoiceViewModel(private val retrofitRepository: RetrofitRepository): ViewModel() {

    private val _transactionById = MutableLiveData<TransactionByIdResponse>()
    val transactionById: LiveData<TransactionByIdResponse> = _transactionById

    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> = _userResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getTransactionById(token: String, transactionId: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getTransactionById(token, transactionId)
            _transactionById.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun getUser(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val response = retrofitRepository.getUser(token)
            _userResponse.postValue(response)
            _isLoading.postValue(false)
        }
    }

}