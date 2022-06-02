package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class MakeOrderResponse(

    @field:SerializedName("data")
    val data: TransactionData? = null,

    @field:SerializedName("info")
    val info: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
)

data class TransactionData(

    @field:SerializedName("qty_transaction")
    val qtyTransaction: Int,

    @field:SerializedName("total_price")
    val totalPrice: Int,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("subtotal_products")
    val subtotalProducts: Int,

    @field:SerializedName("shipping_cost")
    val shippingCost: Int,

    @field:SerializedName("user_id")
    val userId: Int,

    @field:SerializedName("address_id")
    val addressId: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("invoice_number")
    val invoiceNumber: String,

    @field:SerializedName("status")
    val status: String,
)
