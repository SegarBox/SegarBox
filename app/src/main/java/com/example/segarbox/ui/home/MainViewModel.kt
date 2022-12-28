package com.example.segarbox.ui.home

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.body.MostPopularBody
import com.example.core.domain.model.Cart
import com.example.core.domain.model.City
import com.example.core.domain.model.Product
import com.example.core.domain.usecase.HomeUseCase
import com.example.core.utils.Code
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val homeUseCase: HomeUseCase) : ViewModel() {

    private val _checkedChips = MutableLiveData<String>()
    val checkedChips: LiveData<String> = _checkedChips

    private val _mostPopularProductIds = MutableLiveData<Event<List<String>>>()
    val mostPopularProductIds: LiveData<Event<List<String>>> = _mostPopularProductIds

    private val _getCityFromApiResponse = MutableLiveData<Event<Resource<List<City>>>>()
    val getCityFromApiResponse: LiveData<Event<Resource<List<City>>>> = _getCityFromApiResponse

    private val _getProductResponse = MutableLiveData<Event<Resource<List<Product>>>>()
    val getProductResponse: LiveData<Event<Resource<List<Product>>>> = _getProductResponse

    private val _getAllProductsResponse = MutableLiveData<Event<Resource<List<Product>>>>()
    val getAllProductsResponse: LiveData<Event<Resource<List<Product>>>> = _getAllProductsResponse

    private val _getCartResponse = MutableLiveData<Event<Resource<List<Cart>>>>()
    val getCartResponse: LiveData<Event<Resource<List<Cart>>>> = _getCartResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    init {
        // Most Popular
        saveCheckedChips(Code.MOST_POPULAR_CHIPS)
        // All Products
        getAllProducts(1, 20)
    }

    fun saveCheckedChips(chips: String) {
        _checkedChips.postValue(chips)
    }

    fun saveMostPopularProductIds(listProductId: List<String>) {
        _mostPopularProductIds.postValue(Event(listProductId))
    }

    fun getCityFromApi() =
        homeUseCase.getCityFromApi().asLiveData().map {
            _getCityFromApiResponse.postValue(Event(it))
        }

    fun insertCityToDb(listCity: List<City>) =
        homeUseCase.insertCityToDb(listCity)

    fun getCityCount(): LiveData<Event<Resource<Int>>> =
        homeUseCase.getCityCount().asLiveData().map {
            Event(it)
        }

    fun getAllProducts(page: Int, size: Int) =
        homeUseCase.getAllProducts(page, size).asLiveData().map {
            _getAllProductsResponse.postValue(Event(it))
        }

    fun getProductById(id: Int): LiveData<Event<Resource<Product>>> =
        homeUseCase.getProductById(id).asLiveData().map {
            Event(it)
        }

    fun getProductByCategory(
        page: Int,
        size: Int,
        category: String,
    ) = homeUseCase.getProductByCategory(page, size, category).asLiveData().map {
            _getProductResponse.postValue(Event(it))
        }

    fun getProductByMostPopular(mostPopularBody: MostPopularBody) =
        homeUseCase.getProductByMostPopular(mostPopularBody).asLiveData().map {
            _getProductResponse.postValue(Event(it))
        }

    fun getCart(token: String) =
        homeUseCase.getCart(token).asLiveData().map {
            _getCartResponse.postValue(Event(it))
        }

    fun getIntro(): LiveData<Event<Boolean>> =
        homeUseCase.getIntro().asLiveData().map {
            Event(it)
        }

    fun getToken(): LiveData<Event<String>> =
        homeUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun saveIntro(isAlreadyIntro: Boolean) =
        homeUseCase.saveIntro(isAlreadyIntro)

    fun setLoading(isLoading: Boolean) {
        _isLoading.postValue(Event(isLoading))
    }

//    private var _cityFromApi = MutableLiveData<CityResponse>()
//    val cityFromApi: LiveData<CityResponse> = _cityFromApi
//
//    private var _recommendationSystem = MutableLiveData<Event<List<String>>>()
//    val recommendationSystem: LiveData<Event<List<String>>> = _recommendationSystem
//
//    private var _allProductResponse = MutableLiveData<ProductResponse>()
//    val allProductResponse: LiveData<ProductResponse> = _allProductResponse
//
//    private var _productResponse = MutableLiveData<Event<ProductResponse>>()
//    val productResponse: LiveData<Event<ProductResponse>> = _productResponse

//    private var _userCart = MutableLiveData<Event<UserCartResponse>>()
//    val userCart: LiveData<Event<UserCartResponse>> = _userCart
//
//    private var _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading

//    fun getCityFromApi() {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val cityResponse = retrofitRepository.getCityFromApi()
//            _cityFromApi.postValue(cityResponse)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun insertCityToDB(cityResults: List<CityResults>) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            roomRepository.insertCity(cityResults)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun getCityCount(): LiveData<Int> = roomRepository.getCityCount()
//
//    fun getRecommendationSystem(userId: Int) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getRecommendationSystem(userId)
//            _recommendationSystem.postValue(Event(request))
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun getMostPopularProduct(mostPopularBody: MostPopularBody) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getMostPopularProduct(mostPopularBody)
//            _productResponse.postValue(Event(request))
//            _isLoading.postValue(false)
//        }
//    }
//
//
//    private fun getAllProduct() {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getAllProduct(1, 20)
//            _allProductResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun getCategoryProduct(page: Int, size: Int, category: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getCategoryProduct(page, size, category)
//            _productResponse.postValue(Event(request))
//            _isLoading.postValue(false)
//        }
//    }

//    fun getUserCart(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getUserCart(token)
//            _userCart.postValue(Event(request))
//            _isLoading.postValue(false)
//        }
//    }
}