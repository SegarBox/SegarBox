package com.example.segarbox.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AddCartResponse(

    @field:SerializedName("data")
	val data: CartItem? = null,

    @field:SerializedName("info")
	val info: String? = null,

    @field:SerializedName("message")
	val message: String? = null
)

data class CartItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("product_id")
	val productId: Int,

	@field:SerializedName("is_checked")
	val isChecked: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("product_qty")
	val productQty: Int,

	@field:SerializedName("id")
	val id: Int
)
