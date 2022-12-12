package com.example.core.domain.body

import com.google.gson.annotations.SerializedName

data class MostPopularBody(
    @field:SerializedName("product_id")
    val listProductId: List<String>
)
