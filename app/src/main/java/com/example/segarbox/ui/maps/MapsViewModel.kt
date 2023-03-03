package com.example.segarbox.ui.maps

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Maps
import com.example.core.domain.usecase.MapsUseCase
import com.example.core.utils.Event
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val mapsUseCase: MapsUseCase) : ViewModel() {

    private val _getTokenResponse = MutableLiveData<Event<String>>()
    val getTokenResponse: LiveData<Event<String>> = _getTokenResponse

    private val _getLatLng = MutableLiveData<Event<LatLng>>()
    val getLatLng: LiveData<Event<LatLng>> = _getLatLng

    private val _getAddressResponse = MutableLiveData<Event<Resource<List<Maps>>>>()
    val getAddressResponse: LiveData<Event<Resource<List<Maps>>>> = _getAddressResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getToken() = viewModelScope.launch(Dispatchers.IO) {
        mapsUseCase.getToken().collect {
            _getTokenResponse.postValue(Event(it))
        }
    }

    fun getAddress(latLng: String) = viewModelScope.launch(Dispatchers.IO) {
        mapsUseCase.getAddress(latLng).collect {
            _getAddressResponse.postValue(Event(it))
        }
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

}