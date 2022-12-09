package com.example.segarbox.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.RetrofitRepository
import com.example.core.data.RoomRepository
import com.example.core.data.source.remote.response.CityResponse
import com.example.core.data.source.remote.response.CityResults
import com.example.core.data.source.remote.response.ProductResponse
import com.example.core.data.source.remote.response.UserCartResponse
import com.example.core.domain.model.MostPopularBody
import com.example.core.utils.Code
import com.example.core.utils.Event
import kotlinx.coroutines.launch

class MainViewModel(
    private val roomRepository: RoomRepository,
    private val retrofitRepository: RetrofitRepository,
) : ViewModel() {

    private var _cityFromApi = MutableLiveData<CityResponse>()
    val cityFromApi: LiveData<CityResponse> = _cityFromApi

    private var _recommendationSystem = MutableLiveData<Event<List<String>>>()
    val recommendationSystem: LiveData<Event<List<String>>> = _recommendationSystem

    private var _allProductResponse = MutableLiveData<ProductResponse>()
    val allProductResponse: LiveData<ProductResponse> = _allProductResponse

    private var _productResponse = MutableLiveData<Event<ProductResponse>>()
    val productResponse: LiveData<Event<ProductResponse>> = _productResponse

    private var _checkedChips = MutableLiveData<String>()
    val checkedChips: LiveData<String> = _checkedChips

    private var _listProductId = MutableLiveData<Event<List<String>>>()
    val listProductId: LiveData<Event<List<String>>> = _listProductId

    private var _userCart = MutableLiveData<Event<UserCartResponse>>()
    val userCart: LiveData<Event<UserCartResponse>> = _userCart

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        // Most Popular
        saveCheckedChips(Code.MOST_POPULAR_CHIPS)
        // All Products
        getAllProduct()
    }

    fun getCityFromApi() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val cityResponse = retrofitRepository.getCityFromApi()
            _cityFromApi.postValue(cityResponse)
            _isLoading.postValue(false)
        }
    }

    fun insertCityToDB(cityResults: List<CityResults>) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            roomRepository.insertCity(cityResults)
            _isLoading.postValue(false)
        }
    }

    fun getCityCount(): LiveData<Int> = roomRepository.getCityCount()

    fun getRecommendationSystem(userId: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getRecommendationSystem(userId)
            _recommendationSystem.postValue(Event(request))
            _isLoading.postValue(false)
        }
    }

    fun getMostPopularProduct(mostPopularBody: MostPopularBody) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getMostPopularProduct(mostPopularBody)
            _productResponse.postValue(Event(request))
            _isLoading.postValue(false)
        }
    }


    private fun getAllProduct() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getAllProduct(1, 20)
            _allProductResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun getCategoryProduct(page: Int, size: Int, category: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getCategoryProduct(page, size, category)
            _productResponse.postValue(Event(request))
            _isLoading.postValue(false)
        }
    }


    fun saveCheckedChips(chips: String) {
        _checkedChips.postValue(chips)
    }

    fun saveListProductId(listProductId: List<String>) {
        _listProductId.postValue(Event(listProductId))
    }

    fun getUserCart(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getUserCart(token)
            _userCart.postValue(Event(request))
            _isLoading.postValue(false)
        }
    }
}