package com.example.segarbox.core.domain.model

import com.google.gson.annotations.SerializedName

data class MostPopularBody(
    @field:SerializedName("product_id")
    val listProductId: List<String>
)
