package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.core.data.source.remote.response.DeleteAddressResponse
import com.example.segarbox.core.data.source.remote.response.GetAddressResponse
import com.example.segarbox.core.data.RetrofitRepository
import kotlinx.coroutines.launch

class AddressViewModel(private val retrofitRepository: RetrofitRepository): ViewModel() {

    private val _addressResponse = MutableLiveData<GetAddressResponse>()
    val addressResponse: LiveData<GetAddressResponse> = _addressResponse

    private val _deleteAddressResponse = MutableLiveData<DeleteAddressResponse>()
    val deleteAddressResponse: LiveData<DeleteAddressResponse> = _deleteAddressResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserAddresses(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getUserAddresses(token)
            _addressResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun deleteAddress(token: String, addressId: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.deleteAddress(token, addressId)
            _deleteAddressResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }
}