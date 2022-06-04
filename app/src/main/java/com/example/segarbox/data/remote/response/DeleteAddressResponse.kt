package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class DeleteAddressResponse(

    @field:SerializedName("data")
	val data: AddressItem? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("info")
	val info: String? = null
)

