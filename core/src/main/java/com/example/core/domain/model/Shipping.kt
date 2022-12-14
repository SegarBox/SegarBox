package com.example.core.domain.model

import android.os.Parcelable
import com.example.core.data.source.remote.response.CostItem
import com.example.core.data.source.remote.response.CostsItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shipping(

    val costs: List<ShippingCost>,

    val code: String,

    val name: String,

    ) : Parcelable

@Parcelize
data class ShippingCost(

    val cost: List<ShippingCostItem>,

    val service: String,

    val description: String,

    ) : Parcelable

@Parcelize
data class ShippingCostItem(

    val note: String,

    val etd: String,

    val value: Int,

    ) : Parcelable



