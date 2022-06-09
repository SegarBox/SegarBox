package com.example.segarbox.data.local.model

import android.os.Parcelable
import com.example.segarbox.data.local.static.Code
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressModel(
    val street: String = Code.LOCATION_NOT_FOUND,
    val city: String = Code.LOCATION_NOT_FOUND,
    val postalCode: String = Code.LOCATION_NOT_FOUND,
): Parcelable
