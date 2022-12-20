package com.example.core.domain.model

import android.os.Parcelable
import com.example.core.data.source.remote.response.ProductItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(

    val transactionId: Int,

    val image: String,

    val product: Product,

    val rating: Int,

    val createdAt: String,

    val label: String,

    val isRating: Int,

    val updatedAt: String,

    val size: Int,

    val userId: Int,

    val productId: Int,

    val id: Int,

    val invoiceNumber: String

): Parcelable
