package com.example.segarbox.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AddAddressResponse(

	@field:SerializedName("data")
	val data: AddressItem? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("info")
	val info: String? = null
)

@Parcelize
data class AddressItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("street")
	val street: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("postal_code")
	val postalCode: String
) : Parcelable
