package com.example.segarbox.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("errors")
	val errors: Errors? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class User(

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