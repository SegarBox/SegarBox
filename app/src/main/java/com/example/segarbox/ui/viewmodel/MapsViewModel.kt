package com.example.segarbox.ui.viewmodel

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.local.model.DummyAddress
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.util.*

class MapsViewModel : ViewModel() {

    private var _getLatLng = MutableLiveData<LatLng>()
    val getLatLng: LiveData<LatLng> = _getLatLng

    private var _getAddress = MutableLiveData<DummyAddress>()
    val getAddress: LiveData<DummyAddress> = _getAddress

    fun setAddress(context: Context, latLng: LatLng) {
        viewModelScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())

            try {
                val location = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1
                )
                _getAddress.postValue(
//                    location[0].getAddressLine(0)
                    DummyAddress(
                        address = location[0].getAddressLine(0),
                        city = location[0].locality,
                        postalCode = location[0].postalCode,
                    )
                )

            } catch (ex: Exception) {
                _getAddress.postValue(
//                    "Location Error"
                    DummyAddress(
                        address = ex.message!!,
                    )
                )
            }
        }
    }

    fun setLatLng(latLng: LatLng) {
        _getLatLng.postValue(latLng)
    }
}