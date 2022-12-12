package com.example.core.domain.body

import com.google.gson.annotations.SerializedName

data class UpdateStatusBody(
    @field:SerializedName("product_transactions")
    val productTransactions: List<ProductId>
)
