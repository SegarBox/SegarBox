package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.segarbox.data.local.database.MainDatabase
import com.example.segarbox.data.remote.api.ApiServices
import com.example.segarbox.data.remote.response.CityResponse
import com.example.segarbox.data.remote.response.CityResults
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.data.remote.response.ProductResponse
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.data.repository.RoomRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val roomRepository: RoomRepository,
    private val retrofitRepository: RetrofitRepository,
) : ViewModel() {

    private var _cityFromApi = MutableLiveData<CityResponse>()
    val cityFromApi: LiveData<CityResponse> = _cityFromApi

    private var _allProductResponse = MutableLiveData<ProductResponse>()
    val allProductResponse: LiveData<ProductResponse> = _allProductResponse

    private var _productResponse = MutableLiveData<ProductResponse>()
    val productResponse: LiveData<ProductResponse> = _productResponse

    private var _checkedChips = MutableLiveData<String>()
    val checkedChips: LiveData<String> = _checkedChips

    init {
        // Ceritanya ntar most popular dlu
        getLabelProduct(1, 5, "bayam")
        // All Products
        getAllProduct(1, 20)
    }

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


    fun getAllProduct(page: Int, size: Int) {
        viewModelScope.launch {
            val request = retrofitRepository.getAllProduct(page, size)
            _allProductResponse.postValue(request)
        }
    }

    fun getCategoryProduct(page: Int, size: Int, category: String) {
        viewModelScope.launch {
            val request = retrofitRepository.getCategoryProduct(page, size, category)
            _productResponse.postValue(request)
        }
    }

    fun getLabelProduct(page: Int, size: Int, label: String) {
        viewModelScope.launch {
            val request = retrofitRepository.getLabelProduct(page, size, label)
            _productResponse.postValue(request)
        }
    }

    fun saveCheckedChips(chips: String) {
        _checkedChips.postValue(chips)
    }
}