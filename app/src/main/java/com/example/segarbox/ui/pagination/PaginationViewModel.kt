package com.example.segarbox.ui.pagination

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.data.Repository
import com.example.core.data.Resource
import com.example.core.data.source.remote.network.ApiServices
import com.example.core.data.source.remote.response.ProductItem
import com.example.core.data.source.remote.response.UserCartResponse
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Product
import com.example.core.domain.usecase.SearchUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaginationViewModel @Inject constructor(private val searchUseCase: SearchUseCase) : ViewModel() {

    private val _getProductPagingResponse = MutableLiveData<Event<PagingData<Product>>>()
    val getProductPagingResponse: LiveData<Event<PagingData<Product>>> = _getProductPagingResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getProductPaging(
        filter: String,
        filterValue: String,
    ) {
        setLoading(true)
        searchUseCase.getProductPaging(filter, filterValue).asLiveData().map {
            _getProductPagingResponse.postValue(Event(it))
        }
        setLoading(false)
    }

    fun getCart(token: String): LiveData<Event<Resource<List<Cart>>>> =
        searchUseCase.getCart(token).asLiveData().map {
            Event(it)
        }

    fun getToken(): LiveData<Event<String>> =
        searchUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

//    private var _userCart = MutableLiveData<UserCartResponse>()
//    val userCart: LiveData<UserCartResponse> = _userCart
//
//    private var _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun getProductPaging(
//        apiServices: ApiServices,
//        filter: String,
//        filterValue: String,
//    ): LiveData<PagingData<ProductItem>> {
//        return retrofitRepository.getProductPaging(apiServices, filter, filterValue)
//            .cachedIn(viewModelScope)
//    }
//
//    fun getUserCart(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getUserCart(token)
//            _userCart.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
}