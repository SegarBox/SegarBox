package com.example.segarbox.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProductByIdResponse(

	@field:SerializedName("data")
	val data: ProductItem? = null
)
