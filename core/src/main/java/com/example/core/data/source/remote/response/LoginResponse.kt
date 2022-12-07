package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("user")
	val user: UserItem? = null,

    @field:SerializedName("token")
	val token: String? = null,

    @field:SerializedName("errors")
	val errors: ErrorsItem? = null
)


data class UserItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String
)

data class ErrorsItem(

	@field:SerializedName("password")
	val password: List<String>? = null,

	@field:SerializedName("email")
	val email: List<String>? = null
)
