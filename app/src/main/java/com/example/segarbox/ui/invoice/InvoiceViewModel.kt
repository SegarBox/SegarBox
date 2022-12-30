package com.example.segarbox.ui.invoice

import androidx.lifecycle.*
import com.example.core.data.Repository
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.TransactionByIdResponse
import com.example.core.data.source.remote.response.TransactionsStatusResponse
import com.example.core.data.source.remote.response.UserResponse
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.domain.model.Transaction
import com.example.core.domain.model.User
import com.example.core.domain.usecase.InvoiceUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(private val invoiceUseCase: InvoiceUseCase) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    private val _getUserResponse = MutableLiveData<Event<Resource<User>>>()
    val getUserResponse: LiveData<Event<Resource<User>>> = _getUserResponse

    private val _getTransactionByIdResponse = MutableLiveData<Event<Resource<Transaction>>>()
    val getTransactionByIdResponse: LiveData<Event<Resource<Transaction>>> = _getTransactionByIdResponse

    fun getTransactionById(
        token: String,
        transactionId: Int,
    ) = invoiceUseCase.getTransactionById(token, transactionId).asLiveData().map {
            _getTransactionByIdResponse.postValue(Event(it))
        }

    fun getUser(token: String) =
        invoiceUseCase.getUser(token).asLiveData().map {
            _getUserResponse.postValue(Event(it))
        }

    fun updateTransactionStatus(
        token: String,
        transactionId: Int,
        updateStatusBody: UpdateStatusBody,
    ): LiveData<Event<Resource<String>>> =
        invoiceUseCase.updateTransactionStatus(token, transactionId, updateStatusBody).asLiveData()
            .map {
                Event(it)
            }

    fun getToken(): LiveData<Event<String>> =
        invoiceUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

//    private val _transactionById = MutableLiveData<TransactionByIdResponse>()
//    val transactionById: LiveData<TransactionByIdResponse> = _transactionById
//
//    private val _userResponse = MutableLiveData<UserResponse>()
//    val userResponse: LiveData<UserResponse> = _userResponse
//
//    private val _updateTransactionStatusResponse = MutableLiveData<TransactionsStatusResponse>()
//    val updateTransactionStatusResponse: LiveData<TransactionsStatusResponse> = _updateTransactionStatusResponse
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun getTransactionById(token: String, transactionId: Int) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getTransactionById(token, transactionId)
//            _transactionById.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun getUser(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val response = retrofitRepository.getUser(token)
//            _userResponse.postValue(response)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun updateTransactionStatus(token: String, transactionId: Int, updateStatusBody: UpdateStatusBody) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val response = retrofitRepository.updateTransactionStatus(token, transactionId, updateStatusBody)
//            _updateTransactionStatusResponse.postValue(response)
//            _isLoading.postValue(false)
//        }
//    }

}