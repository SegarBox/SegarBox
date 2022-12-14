package com.example.core.domain.usecase

import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.data.source.remote.network.ApiServices
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {

    fun getProductPaging(
        apiServices: ApiServices,
        filter: String,
        filterValue: String,
    ): Flow<PagingData<Product>>

    fun getCart(token: String): Flow<Resource<List<Cart>>>

}