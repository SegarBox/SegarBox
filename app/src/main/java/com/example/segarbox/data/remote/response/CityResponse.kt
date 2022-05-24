package com.example.segarbox.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CityResponse(

	@field:SerializedName("rajaongkir")
	val rajaongkir: Rajaongkir = Rajaongkir()
)

@Entity(tableName = "city")
data class CityResults(

	@field:SerializedName("city_name")
	val cityName: String,

	@field:SerializedName("province")
	val province: String,

	@field:SerializedName("province_id")
	val provinceId: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("postal_code")
	val postalCode: String,

	@PrimaryKey
	@field:SerializedName("city_id")
	val cityId: String
)

data class Status(

	@field:SerializedName("code")
	val code: Int = 404,

	@field:SerializedName("description")
	val description: String = "City Not Found"
)

data class Rajaongkir(

	@field:SerializedName("query")
	val query: List<Any> = listOf(),

	@field:SerializedName("results")
	val results: List<CityResults> = listOf(),

	@field:SerializedName("status")
	val status: Status = Status()
)
