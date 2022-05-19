package com.example.segarbox.data.local.model

import com.example.segarbox.R

data class DummyModel(
    val image: Int = R.drawable.cauliflowers,
    val qty: String = "100g / pack",
    val price: String = "Rp 12000"
)

data class DummyModelInProgress(
    val firstItemImage: Int = R.drawable.cauliflowers,
    val firstItemName: String = "Bayam",
    val firstItemPrice: Int = 4000,
    val firstItemCount: Int = 1,

)
