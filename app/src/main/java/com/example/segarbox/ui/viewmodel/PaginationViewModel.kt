package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.segarbox.data.local.database.MainDatabase
import com.example.segarbox.data.remote.api.ApiServices
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.data.repository.RetrofitRepository

class PaginationViewModel(private val retrofitRepository: RetrofitRepository) : ViewModel() {

    fun getProductPaging(
        apiServices: ApiServices,
        database: MainDatabase,
        filter: String,
        filterValue: String,
    ): LiveData<PagingData<ProductItem>> {
        return retrofitRepository.getProductPaging(apiServices, database, filter, filterValue)
            .cachedIn(viewModelScope)
    }
}