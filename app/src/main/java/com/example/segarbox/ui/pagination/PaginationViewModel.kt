package com.example.segarbox.ui.pagination

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Product
import com.example.core.domain.usecase.SearchUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaginationViewModel @Inject constructor(private val searchUseCase: SearchUseCase) :
    ViewModel() {

    private val _getProductPagingResponse = MutableLiveData<Event<Resource<PagingData<Product>>>>()
    val getProductPagingResponse: LiveData<Event<Resource<PagingData<Product>>>> = _getProductPagingResponse

    private val _getCartResponse = MutableLiveData<Event<Resource<List<Cart>>>>()
    val getCartResponse: LiveData<Event<Resource<List<Cart>>>> = _getCartResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getProductPaging(
        filter: String,
        filterValue: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        searchUseCase.getProductPaging(filter, filterValue).collect {
            _getProductPagingResponse.postValue(Event(it))
        }
    }

    fun getCart(token: String) = viewModelScope.launch(Dispatchers.IO) {
        searchUseCase.getCart(token).collect {
            _getCartResponse.postValue(Event(it))
        }
    }

    fun getToken(): LiveData<Event<String>> =
        searchUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

}