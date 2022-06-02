package com.example.segarbox.data.local.model

import com.google.gson.annotations.SerializedName

data class ProductTransactions(
    @field:SerializedName("product_id")
    val product_id: Int,

    @field:SerializedName("qty")
    val qty: Int
)
