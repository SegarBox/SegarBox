package com.example.segarbox.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GetAddressResponse(

    @field:SerializedName("data")
	val data: List<AddressItem>? = null,

    @field:SerializedName("message")
	val message: String? = null
)

