package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(

    val id: Int,

    val userId: Int,

    val street: String,

    val city: String,

    val postalCode: String

): Parcelable
