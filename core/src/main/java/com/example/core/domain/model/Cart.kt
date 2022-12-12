package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(

    val id: Int,

    val userId: Int,

    val productId: Int,

    val product: Product,

    val productQty: Int,

    val isChecked: Int

): Parcelable
