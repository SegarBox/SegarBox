package com.example.core.domain.interactor

import com.example.core.data.Resource
import com.example.core.domain.model.Address
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartDetail
import com.example.core.domain.repository.IRepository
import com.example.core.domain.usecase.AddressUseCase
import com.example.core.domain.usecase.CartUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartInteractor @Inject constructor(private val repository: IRepository) : CartUseCase {

    override fun getCart(token: String): Flow<Resource<List<Cart>>> = repository.getCart(token)

    override fun getCheckedCart(token: String): Flow<Resource<List<Cart>>> =
        repository.getCheckedCart(token)

    override fun deleteCart(token: String, cartId: Int): Flow<Resource<String>> =
        repository.deleteCart(token, cartId)

    override fun updateCart(
        token: String,
        cartId: Int,
        productId: Int,
        productQty: Int,
        isChecked: Int,
    ): Flow<Resource<String>> =
        repository.updateCart(token, cartId, productId, productQty, isChecked)

    override fun getCartDetail(token: String, shippingCost: Int): Flow<Resource<CartDetail>> =
        repository.getCartDetail(token, shippingCost)

}