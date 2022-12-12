package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartDetail(

    val qtyTransaction: Int = 0,

    val totalPrice: Int = 0,

    val subtotalProducts: Int = 0,

    val shippingCost: Int = 0

): Parcelable
