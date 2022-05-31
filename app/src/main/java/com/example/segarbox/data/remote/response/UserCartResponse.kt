package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserCartResponse(

	@field:SerializedName("data")
	val data: List<UserCartItem>? = null,
)

data class UserCartItem(

	@field:SerializedName("product")
	val product: ProductItem,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("product_id")
	val productId: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("is_checked")
	val isChecked: Int,

	@field:SerializedName("product_qty")
	val productQty: Int,

	@field:SerializedName("id")
	val id: Int
)

