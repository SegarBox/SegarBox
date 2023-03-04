package com.example.segarbox.ui.invoice

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.domain.model.Transaction
import com.example.core.domain.model.User
import com.example.core.domain.usecase.InvoiceUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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
    ) = viewModelScope.launch(Dispatchers.IO) {
        invoiceUseCase.getTransactionById(token, transactionId).collect {
            _getTransactionByIdResponse.postValue(Event(it))
        }
    }

    fun getUser(token: String) = viewModelScope.launch(Dispatchers.IO) {
        invoiceUseCase.getUser(token).collect {
            _getUserResponse.postValue(Event(it))
        }
    }

    fun updateTransactionStatus(
        token: String,
        transactionId: Int,
        updateStatusBody: UpdateStatusBody,
    ): LiveData<Event<Resource<String>>> =
        invoiceUseCase.updateTransactionStatus(token, transactionId, updateStatusBody).asLiveData()
            .map { Event(it) }

    fun getToken(): LiveData<Event<String>> =
        invoiceUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

}