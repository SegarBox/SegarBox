package com.example.segarbox.data.local.model

import android.os.Parcelable
import com.example.segarbox.R
import kotlinx.parcelize.Parcelize

data class DummyModel(
    val image: Int = R.drawable.redvelvet,
    val qty: String = "100g / pack",
    val price: String = "Rp 12000"
)
