package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Maps(

    val formattedAddress: String,

    val addressComponents: List<AddressComponents>,

    ): Parcelable

@Parcelize
data class AddressComponents(

    val types: List<String>,

    val shortName: String,

    val longName: String

): Parcelable
