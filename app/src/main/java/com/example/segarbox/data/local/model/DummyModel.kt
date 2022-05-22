package com.example.segarbox.data.local.model

import com.example.segarbox.R

data class DummyModel(
    val image: Int = R.drawable.cauliflowers,
    val qty: String = "100g / pack",
    val price: String = "Rp 12000"
)

data class DummyModelTransaction(
    val firstItemImage: Int = R.drawable.cauliflowers,
    val firstItemName: String = "Cauliflower",
    val firstItemCount: Int = 1,
    val totalMoreProductCount: Int = 2,
    val grandTotal: Int = 13000
)

data class DummyModelCart(
    val isChecked: Int = 0,
    val image: Int = R.drawable.cauliflowers,
    val itemName: String = "Cauliflower",
    val qty: String = "100g / pack",
    val price: Int = 4000,
    val count: Int = 1
)

data class DummyAddress(
    val address: String = "Jl Puri Anjasmoro A11 no 34, Semarang",
    val city: String = "",
    val postalCode: String = "",
)
