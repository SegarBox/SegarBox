package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionProduct(

    val image: String,

    val size: Int,

    val price: Int,

    val qty: Int,

    val productQty: Int,

    val id: Int,

    val productId: Int,

    val label: String,

    val detail: String,

    val category: String,

): Parcelable
