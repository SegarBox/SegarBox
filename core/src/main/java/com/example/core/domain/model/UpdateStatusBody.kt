package com.example.core.domain.model

import com.google.gson.annotations.SerializedName

data class UpdateStatusBody(
    @field:SerializedName("product_transactions")
    val productTransations: List<ProductId>
)
