package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.remote.response.CityResponse
import com.example.segarbox.data.remote.response.CityResults
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.data.repository.RoomRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val roomRepository: RoomRepository,
    private val retrofitRepository: RetrofitRepository,
) : ViewModel() {

    private var _cityFromApi = MutableLiveData<CityResponse>()
    val cityFromApi: LiveData<CityResponse> = _cityFromApi

    fun getCityFromApi() {
        viewModelScope.launch {
            val cityResponse = retrofitRepository.getCityFromApi()
            _cityFromApi.postValue(cityResponse)
        }
    }

    fun insertCityToDB(cityResults: List<CityResults>) {
        viewModelScope.launch {
            roomRepository.insertCity(cityResults)
        }
    }

    fun getCityCount(): LiveData<Int> = roomRepository.getCityCount()
}