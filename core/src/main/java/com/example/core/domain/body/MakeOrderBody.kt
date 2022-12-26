package com.example.core.domain.body

import com.google.gson.annotations.SerializedName

data class MakeOrderBody(
    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("address_id")
    val addressId: Int,

    @field:SerializedName("qty_transaction")
    val qtyTransaction: Int,

    @field:SerializedName("subtotal_products")
    val subtotalProducts: Int,

    @field:SerializedName("total_price")
    val totalPrice: Int,

    @field:SerializedName("shipping_cost")
    val shippingCost: Int,

    @field:SerializedName("product_transactions")
    val productTransactions: List<ProductTransactions>
)
