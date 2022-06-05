package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class SaveRatingResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("info")
	val info: String
)

data class Data(

	@field:SerializedName("transaction_id")
	val transactionId: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("product_id")
	val productId: Int,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("is_rating")
	val isRating: Int
)
