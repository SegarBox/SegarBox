package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProductByIdResponse(

	@field:SerializedName("data")
	val data: ProductItem? = null
)
