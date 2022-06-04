package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class TransactionsResponse(

    @field:SerializedName("data")
    val data: List<TransactionItem>? = null,

    @field:SerializedName("message")
    val message: String? = null,
)

