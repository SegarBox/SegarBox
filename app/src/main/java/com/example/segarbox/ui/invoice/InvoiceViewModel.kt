package com.example.segarbox.ui.invoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.RetrofitRepository
import com.example.core.data.source.remote.response.TransactionByIdResponse
import com.example.core.data.source.remote.response.TransactionsStatusResponse
import com.example.core.data.source.remote.response.UserResponse
import com.example.core.domain.body.UpdateStatusBody
import kotlinx.coroutines.launch

class InvoiceViewModel(private val retrofitRepository: RetrofitRepository): ViewModel() {

    private val _transactionById = MutableLiveData<TransactionByIdResponse>()
    val transactionById: LiveData<TransactionByIdResponse> = _transactionById

    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> = _userResponse

    private val _updateTransactionStatusResponse = MutableLiveData<TransactionsStatusResponse>()
    val updateTransactionStatusResponse: LiveData<TransactionsStatusResponse> = _updateTransactionStatusResponse

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

    fun updateTransactionStatus(token: String, transactionId: Int, updateStatusBody: UpdateStatusBody) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val response = retrofitRepository.updateTransactionStatus(token, transactionId, updateStatusBody)
            _updateTransactionStatusResponse.postValue(response)
            _isLoading.postValue(false)
        }
    }

}