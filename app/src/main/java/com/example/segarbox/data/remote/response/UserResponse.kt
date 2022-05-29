package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
	@field:SerializedName("data")
	val data: UserLogin? = null
)
