package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShippingModel(
    var code: String = "",
    var service: String = "",
    var etd: String = "",
    var price: Int = 0
) : Parcelable
