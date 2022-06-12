package com.example.segarbox.data.local.model

import com.google.gson.annotations.SerializedName

data class MostPopularBody(
    @field:SerializedName("product_id")
    val listProductId: List<String>
)
