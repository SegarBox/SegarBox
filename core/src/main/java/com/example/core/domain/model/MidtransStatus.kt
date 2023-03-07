package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MidtransStatus(

    val transactionId: String,

    val statusMessage: String,

    val paymentType: String,

    val transactionStatus: String,

    val statusCode: String,

    val transactionTime: String,

    val currency: String,

    val grossAmount: String,

    val expiryTime: String,

    val orderId: String

) : Parcelable
