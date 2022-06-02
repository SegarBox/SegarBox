package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetAddressResponse(

	@field:SerializedName("data")
	val data: List<AddressData>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

