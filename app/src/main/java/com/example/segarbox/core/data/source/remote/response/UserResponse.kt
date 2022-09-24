package com.example.segarbox.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
	@field:SerializedName("data")
	val data: UserItem? = null
)
