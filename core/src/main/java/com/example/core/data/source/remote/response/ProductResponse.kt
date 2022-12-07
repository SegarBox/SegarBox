package com.example.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ProductResponse(

	@field:SerializedName("data")
	val data: List<ProductItem> = listOf()
)

@Parcelize
data class ProductItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("size")
	val size: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("qty")
	val qty: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("label")
	val label: String,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("category")
	val category: String = "veggies",

): Parcelable
