package com.example.segarbox.ui.shipping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.RoomRepository
import com.example.core.data.source.remote.response.CityResults
import com.example.core.data.source.remote.response.ShippingResponse
import com.example.core.utils.Code
import kotlinx.coroutines.launch

class ShippingViewModel(
    private val roomRepository: RoomRepository,
    private val retrofitRepository: com.example.core.data.RetrofitRepository,
) : ViewModel() {

    private var _destinationId =  MutableLiveData<String>()
    val destinationId: LiveData<String> = _destinationId

    private var _shippingCosts = MutableLiveData<List<ShippingResponse>>()
    val shippingCosts: LiveData<List<ShippingResponse>> = _shippingCosts

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCity(city: String, type: String): LiveData<List<CityResults>> =
        roomRepository.getCity(city, type)

    fun setDestinationId(id: String) {
        _destinationId.postValue(id)
    }

    fun getShippingCosts(
        destination: String,
        weight: String,
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val responseTiki = retrofitRepository.getShippingCosts(destination, weight, Code.TIKI)
            val responseJne = retrofitRepository.getShippingCosts(destination, weight, Code.JNE)
            val responsePos = retrofitRepository.getShippingCosts(destination, weight, Code.POS)

            _shippingCosts.postValue(listOf(responseTiki, responseJne, responsePos))
            _isLoading.postValue(false)
        }
    }

}