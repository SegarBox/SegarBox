package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class TransactionsStatusResponse(

    @field:SerializedName("info")
    val info: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
)

