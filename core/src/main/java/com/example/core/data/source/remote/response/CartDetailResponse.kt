package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CartDetailResponse(

	@field:SerializedName("qty_transaction")
	val qtyTransaction: Int = 0,

	@field:SerializedName("total_price")
	val totalPrice: Int = 0,

	@field:SerializedName("subtotal_products")
	val subtotalProducts: Int = 0,

	@field:SerializedName("shipping_cost")
	val shippingCost: Int = 0
)
