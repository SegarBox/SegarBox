package com.example.core.domain.interactor

import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.data.source.remote.network.ApiServices
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.domain.model.*
import com.example.core.domain.repository.IRepository
import com.example.core.domain.usecase.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchInteractor @Inject constructor(private val repository: IRepository) : SearchUseCase {

    override fun getProductPaging(
        apiServices: ApiServices,
        filter: String,
        filterValue: String,
    ): Flow<PagingData<Product>> = repository.getProductPaging(apiServices, filter, filterValue)

    override fun getCart(token: String): Flow<Resource<List<Cart>>> = repository.getCart(token)

}