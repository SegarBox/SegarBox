package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ShippingResponse(

    @field:SerializedName("rajaongkir")
    val rajaongkir: Shipping? = null,
)

data class Shipping(

    @field:SerializedName("results")
    val results: List<ShippingResults>,
)

data class ShippingResults(

    @field:SerializedName("costs")
    val costs: List<CostsItem>,

    @field:SerializedName("code")
    val code: String,

    @field:SerializedName("name")
    val name: String,
)

data class CostsItem(

    @field:SerializedName("cost")
    val cost: List<CostItem>,

    @field:SerializedName("service")
    val service: String,

    @field:SerializedName("description")
    val description: String,
)

data class CostItem(

    @field:SerializedName("note")
    val note: String,

    @field:SerializedName("etd")
    val etd: String,

    @field:SerializedName("value")
    val value: Int,
)
