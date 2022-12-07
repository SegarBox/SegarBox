package com.example.segarbox.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.RetrofitRepository
import com.example.core.data.source.remote.response.AddAddressResponse
import com.example.core.data.source.remote.response.MapsResponse
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class MapsViewModel(private val retrofitRepository: RetrofitRepository) : ViewModel() {

    private var _getLatLng = MutableLiveData<LatLng>()
    val getLatLng: LiveData<LatLng> = _getLatLng

    private var _addressResponse = MutableLiveData<AddAddressResponse>()
    val addressResponse: LiveData<AddAddressResponse> = _addressResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private var _address = MutableLiveData<MapsResponse>()
    val address: LiveData<MapsResponse> = _address

    fun getAddress(latLng: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val mapsResponse = retrofitRepository.getAddress(latLng)
            _address.postValue(mapsResponse)
            _isLoading.postValue(false)
        }
    }

    fun setLatLng(latLng: LatLng) {
        _getLatLng.postValue(latLng)
    }

    fun saveAddress(token: String, street: String, city: String, postalCode: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.saveAddress(token, street, city, postalCode)
            _addressResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }
}