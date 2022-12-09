package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(

    val cityId: String,

    val cityName: String,

    val provinceId: String,

    val province: String,

    val type: String,

    val postalCode: String

): Parcelable
