package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(

    val id: Int,

    val image: String,

    val label: String,

    val size: Int,

    val price: Int,

    val qty: Int,

    val detail: String,

    val category: String

): Parcelable
