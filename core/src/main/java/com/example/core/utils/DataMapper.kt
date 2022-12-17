package com.example.core.utils

import com.example.core.data.source.remote.response.GetAddressResponse
import com.example.core.data.source.remote.response.ProductItem
import com.example.core.data.source.remote.response.UserCartResponse
import com.example.core.domain.model.Address
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Product

object DataMapper {

    fun mapGetAddressResponseToAddress(getAddressResponse: GetAddressResponse): List<Address>? =
        getAddressResponse.data?.map {
            Address(
                id = it.id,
                userId = it.userId,
                street = it.street,
                city = it.city,
                postalCode = it.postalCode
            )
        }

    fun mapProductItemToProduct(productItem: ProductItem): Product =
        Product(
            id = productItem.id,
            image = productItem.image,
            label = productItem.label,
            size = productItem.size,
            price = productItem.price,
            qty = productItem.qty,
            detail = productItem.detail,
            category = productItem.category
        )

    fun mapCartResponseToCart(userCartResponse: UserCartResponse): List<Cart>? =
        userCartResponse.data?.map {
            Cart(
                id = it.id,
                userId = it.userId,
                productId = it.productId,
                productQty = it.productQty,
                product = mapProductItemToProduct(it.product),
                isChecked = it.isChecked
            )
        }

}