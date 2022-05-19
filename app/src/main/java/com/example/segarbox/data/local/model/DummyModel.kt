package com.example.segarbox.data.local.model

import com.example.segarbox.R

data class DummyModel(
    val image: Int = R.drawable.cauliflowers,
    val qty: String = "100g / pack",
    val price: String = "Rp 12000"
)

data class DummyModelTransaction(
    val firstItemImage: Int = R.drawable.cauliflowers,
    val firstItemName: String = "Cauliflowers",
    val firstItemCount: Int = 1,
    val totalMoreProductCount: Int = 2,
    val grandTotal: Int = 13000
)


