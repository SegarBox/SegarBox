package com.example.segarbox.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TransactionsStatusResponse(

    @field:SerializedName("info")
    val info: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
)

