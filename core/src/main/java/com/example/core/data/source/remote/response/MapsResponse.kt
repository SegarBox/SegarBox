package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MapsResponse(

    @field:SerializedName("results")
	val results: List<ResultsItem>? = null,

    @field:SerializedName("status")
	val status: String,

)

data class ResultsItem(

	@field:SerializedName("formatted_address")
	val formattedAddress: String,

	@field:SerializedName("geometry")
	val geometry: Geometry,

	@field:SerializedName("address_components")
	val addressComponents: List<AddressComponentsItem>,

	)

data class AddressComponentsItem(

	@field:SerializedName("types")
	val types: List<String>,

	@field:SerializedName("short_name")
	val shortName: String,

	@field:SerializedName("long_name")
	val longName: String
)


data class Location(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
)

data class Geometry(

    @field:SerializedName("location")
	val location: Location,

    )
