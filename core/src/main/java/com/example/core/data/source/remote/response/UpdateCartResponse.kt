package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateCartResponse(

    @field:SerializedName("data")
	val data: CartItem? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("info")
	val info: String? = null
)

