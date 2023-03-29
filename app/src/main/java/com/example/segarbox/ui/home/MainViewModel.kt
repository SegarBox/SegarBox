package com.example.segarbox.ui.home

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Product
import com.example.core.domain.usecase.HomeUseCase
import com.example.core.utils.CHIPS_VALUE
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val homeUseCase: HomeUseCase) : ViewModel() {

    private val _checkedChips = MutableLiveData<Event<String>>()
    val checkedChips: LiveData<Event<String>> = _checkedChips

    private val _getProductResponse = MutableLiveData<Event<Resource<List<Product>>>>()
    val getProductResponse: LiveData<Event<Resource<List<Product>>>> = _getProductResponse

    private val _getAllProductsResponse = MutableLiveData<Event<Resource<List<Product>>>>()
    val getAllProductsResponse: LiveData<Event<Resource<List<Product>>>> = _getAllProductsResponse

    private val _getCartResponse = MutableLiveData<Event<Resource<List<Cart>>>>()
    val getCartResponse: LiveData<Event<Resource<List<Cart>>>> = _getCartResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    init {
        // Refresh data jika change theme, karena di init block tidak ngefek
        getTheme()
        // Intro Onboarding
        saveIntro(true)
        // Get City From Rajaongkir
        getCityFromApi()
    }

    private fun getTheme() = viewModelScope.launch(Dispatchers.IO) {
        homeUseCase.getTheme().collect {
            // Most Popular
            saveCheckedChips(CHIPS_VALUE)
            // All Products
            getAllProducts()
        }
    }

    fun saveCheckedChips(chips: String) = _checkedChips.postValue(Event(chips))

    fun getCityFromApi() = viewModelScope.launch(Dispatchers.IO) {
        // Get City Count
        homeUseCase.getCityCount().collect { resourceCount ->
            setLoading(true)
            if (resourceCount is Resource.Success) {
                resourceCount.data?.let { cityCount ->
                    if (cityCount == 0) {
                        homeUseCase.getCityFromApi().collect { resourceCity ->
                            if (resourceCity is Resource.Success) {
                                resourceCity.data?.let { city ->
                                    homeUseCase.insertCityToDb(city)
                                }
                            }
                        }
                    }
                }
            }
            setLoading(false)
        }
    }

    private fun getAllProducts() = viewModelScope.launch(Dispatchers.IO) {
        homeUseCase.getAllProducts(1, 20).collect {
            _getAllProductsResponse.postValue(Event(it))
        }
    }

    fun getProductByCategory(
        page: Int,
        size: Int,
        category: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        homeUseCase.getProductByCategory(page, size, category).collect {
            _getProductResponse.postValue(Event(it))
        }
    }

    fun getProductByMostPopular() = viewModelScope.launch(Dispatchers.IO) {
        homeUseCase.getAllProducts(2, 10).collect {
            _getProductResponse.postValue(Event(it))
        }
    }

    fun getCart(token: String) = viewModelScope.launch(Dispatchers.IO) {
        homeUseCase.getCart(token).collect {
            _getCartResponse.postValue(Event(it))
        }
    }

    fun getToken(): LiveData<Event<String>> =
        homeUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun saveIntro(isAlreadyIntro: Boolean) = homeUseCase.saveIntro(isAlreadyIntro)

    fun setLoading(isLoading: Boolean) = _isLoading.postValue(Event(isLoading))

}