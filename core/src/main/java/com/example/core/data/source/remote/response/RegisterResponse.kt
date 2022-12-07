package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("user")
	val user: UserItem? = null,

    @field:SerializedName("errors")
	val errors: Errors? = null,

    @field:SerializedName("token")
	val token: String? = null
)

data class Errors(

	@field:SerializedName("password")
	val password: List<String>? = null,

	@field:SerializedName("phone")
	val phone: List<String>? = null,

	@field:SerializedName("name")
	val name: List<String>? = null,

	@field:SerializedName("email")
	val email: List<String>? = null
)
