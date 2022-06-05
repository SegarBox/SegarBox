package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class RatingResponse(
	@field:SerializedName("data")
	val data: List<RatingItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,
)

data class RatingItem(

	@field:SerializedName("transaction_id")
	val transactionId: Int,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("product")
	val product: ProductItem,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("label")
	val label: String,

	@field:SerializedName("is_rating")
	val isRating: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("size")
	val size: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("product_id")
	val productId: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("invoice_number")
	val invoiceNumber: String
)

