package com.example.segarbox.ui.maps

import androidx.lifecycle.*
import com.example.core.data.Repository
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.AddAddressResponse
import com.example.core.data.source.remote.response.MapsResponse
import com.example.core.domain.model.Maps
import com.example.core.domain.usecase.MapsUseCase
import com.example.core.utils.Event
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val mapsUseCase: MapsUseCase) : ViewModel() {

    fun getAddress(latLng: String): LiveData<Event<Resource<List<Maps>>>> =
        mapsUseCase.getAddress(latLng).asLiveData().map {
            Event(it)
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

    private var _getLatLng = MutableLiveData<LatLng>()
    val getLatLng: LiveData<LatLng> = _getLatLng

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

    fun setLatLng(latLng: LatLng) {
        _getLatLng.postValue(latLng)
    }

//    fun saveAddress(token: String, street: String, city: String, postalCode: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.saveAddress(token, street, city, postalCode)
//            _addressResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
}