package com.example.segarbox.ui.address

import android.util.Log
import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Address
import com.example.core.domain.usecase.AddressUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val addressUseCase: AddressUseCase): ViewModel() {

    private val _getUserAddressesResponse = MutableLiveData<Event<Resource<List<Address>>>>()
    val getUserAddressesResponse: LiveData<Event<Resource<List<Address>>>> = _getUserAddressesResponse

    private val _getTokenResponse = MutableLiveData<Event<String>>()
    val getTokenResponse: LiveData<Event<String>> = _getTokenResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    init {
        getToken()
    }

    fun getUserAddresses(token: String) = viewModelScope.launch(Dispatchers.IO) {
        addressUseCase.getUserAddresses(token).collect {
            _getUserAddressesResponse.postValue(Event(it))
        }
    }

    fun deleteAddress(token: String, addressId: Int): LiveData<Event<Resource<String>>> =
        addressUseCase.deleteAddress(token, addressId).asLiveData().map {
            Event(it)
        }

    fun getToken() = viewModelScope.launch(Dispatchers.IO) {
        addressUseCase.getToken().collect {
            _getTokenResponse.postValue(Event(it))
        }
    }


    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

}