package com.example.segarbox.ui.maps

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Maps
import com.example.core.domain.usecase.MapsUseCase
import com.example.core.utils.Event
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val mapsUseCase: MapsUseCase) : ViewModel() {

    private val _getLatLng = MutableLiveData<Event<LatLng>>()
    val getLatLng: LiveData<Event<LatLng>> = _getLatLng

    private val _getAddressResponse = MutableLiveData<Event<Resource<List<Maps>>>>()
    val getAddressResponse: LiveData<Event<Resource<List<Maps>>>> = _getAddressResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getToken(): LiveData<Event<String>> =
        mapsUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun getAddress(latLng: String) =
        mapsUseCase.getAddress(latLng).asLiveData().map {
            _getAddressResponse.postValue(Event(it))
        }

    fun saveAddress(
        token: String,
        street: String,
        city: String,
        postalCode: String,
    ): LiveData<Event<Resource<String>>> =
        mapsUseCase.saveAddress(token, street, city, postalCode).asLiveData().map {
            Event(it)
        }

    fun setLatLng(latLng: LatLng) {
        _getLatLng.postValue(Event(latLng))
    }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))


//    private var _addressResponse = MutableLiveData<AddAddressResponse>()
//    val addressResponse: LiveData<AddAddressResponse> = _addressResponse
//
//    private var _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//
//    private var _address = MutableLiveData<MapsResponse>()
//    val address: LiveData<MapsResponse> = _address
//
//    fun getAddress(latLng: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val mapsResponse = retrofitRepository.getAddress(latLng)
//            _address.postValue(mapsResponse)
//            _isLoading.postValue(false)
//        }
//    }
//    fun saveAddress(token: String, street: String, city: String, postalCode: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.saveAddress(token, street, city, postalCode)
//            _addressResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
}