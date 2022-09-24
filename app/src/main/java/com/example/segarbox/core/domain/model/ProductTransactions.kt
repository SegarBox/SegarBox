package com.example.segarbox.core.domain.model

import com.google.gson.annotations.SerializedName

data class ProductTransactions(
    @field:SerializedName("product_id")
    val productId: Int,

    @field:SerializedName("qty")
    val qty: Int
)
