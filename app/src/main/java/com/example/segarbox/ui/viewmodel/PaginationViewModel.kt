package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.segarbox.data.remote.api.ApiServices
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.data.remote.response.UserCartResponse
import com.example.segarbox.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class PaginationViewModel(private val retrofitRepository: RetrofitRepository) : ViewModel() {

    private var _userCart = MutableLiveData<UserCartResponse>()
    val userCart: LiveData<UserCartResponse> = _userCart

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getProductPaging(
        apiServices: ApiServices,
        filter: String,
        filterValue: String,
    ): LiveData<PagingData<ProductItem>> {
        return retrofitRepository.getProductPaging(apiServices, filter, filterValue)
            .cachedIn(viewModelScope)
    }

    fun getUserCart(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getUserCart(token)
            _userCart.postValue(request)
            _isLoading.postValue(false)
        }
    }
}