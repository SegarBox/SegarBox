package com.example.core.domain.interactor

import com.example.core.data.Resource
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.model.Address
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartDetail
import com.example.core.domain.model.MakeOrder
import com.example.core.domain.repository.IRepository
import com.example.core.domain.usecase.AddressUseCase
import com.example.core.domain.usecase.CartUseCase
import com.example.core.domain.usecase.CheckoutUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckoutInteractor @Inject constructor(private val repository: IRepository) :
    CheckoutUseCase {

    override fun getCheckedCart(token: String): Flow<Resource<List<Cart>>> =
        repository.getCheckedCart(token)

    override fun getCartDetail(token: String, shippingCost: Int): Flow<Resource<CartDetail>> =
        repository.getCartDetail(token, shippingCost)

    override fun makeOrder(token: String, makeOrderBody: MakeOrderBody): Flow<Resource<MakeOrder>> =
        repository.makeOrder(token, makeOrderBody)

}