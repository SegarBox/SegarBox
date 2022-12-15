package com.example.core.domain.interactor

import com.example.core.data.Resource
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.model.*
import com.example.core.domain.repository.IRepository
import com.example.core.domain.usecase.AddressUseCase
import com.example.core.domain.usecase.CartUseCase
import com.example.core.domain.usecase.CheckoutUseCase
import com.example.core.domain.usecase.DetailUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailInteractor @Inject constructor(private val repository: IRepository) : DetailUseCase {

    override fun getProductById(id: Int): Flow<Resource<Product>> = repository.getProductById(id)

    override fun getCart(token: String): Flow<Resource<List<Cart>>> = repository.getCart(token)

    override fun addCart(token: String, productId: Int, productQty: Int): Flow<Resource<String>> =
        repository.addCart(token, productId, productQty)

    override fun updateCart(
        token: String,
        cartId: Int,
        productId: Int,
        productQty: Int,
        isChecked: Int,
    ): Flow<Resource<String>> =
        repository.updateCart(token, cartId, productId, productQty, isChecked)

    override fun deleteCart(token: String, cartId: Int): Flow<Resource<String>> =
        repository.deleteCart(token, cartId)

}