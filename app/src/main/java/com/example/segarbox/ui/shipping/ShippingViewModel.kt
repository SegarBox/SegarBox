package com.example.segarbox.ui.shipping

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.City
import com.example.core.domain.model.Shipping
import com.example.core.domain.usecase.ShippingUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShippingViewModel @Inject constructor(private val shippingUseCase: ShippingUseCase) : ViewModel() {

    private val _destinationId =  MutableLiveData<Event<String>>()
    val destinationId: LiveData<Event<String>> = _destinationId

    private val _getShippingCostsResponse = MutableLiveData<Event<Resource<List<Shipping>>>>()
    val getShippingCostsResponse: LiveData<Event<Resource<List<Shipping>>>> = _getShippingCostsResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getCity(city: String, type: String): LiveData<Event<Resource<List<City>>>> =
        shippingUseCase.getCity(city, type).asLiveData().map {
            Event(it)
        }

    fun getShippingCosts(destination: String, weight: String) = viewModelScope.launch(Dispatchers.IO) {
        shippingUseCase.getShippingCosts(destination, weight).collect {
            _getShippingCostsResponse.postValue(Event(it))
        }
    }

    fun setDestinationId(id: String) {
        _destinationId.postValue(Event(id))
    }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

}