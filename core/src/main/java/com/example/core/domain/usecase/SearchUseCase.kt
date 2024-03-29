package com.example.core.domain.usecase

import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {

    fun getProductPaging(
        filter: String,
        filterValue: String,
    ): Flow<Resource<PagingData<Product>>>

    fun getCart(token: String): Flow<Resource<List<Cart>>>

    fun getToken(): Flow<String>

}