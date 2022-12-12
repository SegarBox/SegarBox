package com.example.core.domain.model

import android.os.Parcelable
import com.example.core.data.source.remote.response.AddressItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(

    val productTransactions: List<TransactionProduct>,

    val qtyTransaction: Int,

    val address: AddressItem,

    val totalPrice: Int,

    val subtotalProducts: Int,

    val shippingCost: Int,

    val addressId: Int,

    val userId: Int,

    val id: Int,

    val invoiceNumber: String,

    val status: String,

    ): Parcelable
