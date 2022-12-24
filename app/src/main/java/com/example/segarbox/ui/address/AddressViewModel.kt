package com.example.segarbox.ui.address

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.DeleteAddressResponse
import com.example.core.data.source.remote.response.GetAddressResponse
import com.example.core.domain.model.Address
import com.example.core.domain.usecase.AddressUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val addressUseCase: AddressUseCase): ViewModel() {

    private val _getUserAddressesResponse = MutableLiveData<Event<Resource<List<Address>>>>()
    val getUserAddressesResponse: LiveData<Event<Resource<List<Address>>>> = _getUserAddressesResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getUserAddresses(token: String) =
        addressUseCase.getUserAddresses(token).asLiveData().map {
            _getUserAddressesResponse.postValue(Event(it))
        }

    fun deleteAddress(token: String, addressId: Int): LiveData<Event<Resource<String>>> =
        addressUseCase.deleteAddress(token, addressId).asLiveData().map {
            Event(it)
        }

    fun getToken(): LiveData<Event<String>> =
        addressUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

//    private val _addressResponse = MutableLiveData<GetAddressResponse>()
//    val addressResponse: LiveData<GetAddressResponse> = _addressResponse
//
//    private val _deleteAddressResponse = MutableLiveData<DeleteAddressResponse>()
//    val deleteAddressResponse: LiveData<DeleteAddressResponse> = _deleteAddressResponse
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading

//    fun getUserAddresses(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getUserAddresses(token)
//            _addressResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }

//    fun deleteAddress(token: String, addressId: Int) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.deleteAddress(token, addressId)
//            _deleteAddressResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
}