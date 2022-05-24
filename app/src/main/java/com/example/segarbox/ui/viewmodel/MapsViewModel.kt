package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.remote.response.MapsResponse
import com.example.segarbox.data.repository.RetrofitRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class MapsViewModel(private val retrofitRepository: RetrofitRepository) : ViewModel() {

    private var _getLatLng = MutableLiveData<LatLng>()
    val getLatLng: LiveData<LatLng> = _getLatLng


    private var _address = MutableLiveData<MapsResponse>()
    val address: LiveData<MapsResponse> = _address

    fun getAddress(latLng: String) {
        viewModelScope.launch {
            val mapsResponse = retrofitRepository.getAddress(latLng)
            _address.postValue(mapsResponse)
        }
    }

    fun setLatLng(latLng: LatLng) {
        _getLatLng.postValue(latLng)
    }
}