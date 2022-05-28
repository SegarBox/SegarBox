package com.example.segarbox.data.remote.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ProductResponse(

	@field:SerializedName("data")
	val data: List<ProductItem> = listOf()
)

@Entity(tableName = "product")
@Parcelize
data class ProductItem(

	@field:SerializedName("size")
	val size: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("qty")
	val qty: Int,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("label")
	val label: String,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("category")
	val category: String = "veggies",

): Parcelable
