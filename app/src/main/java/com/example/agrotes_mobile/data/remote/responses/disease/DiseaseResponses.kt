package com.example.agrotes_mobile.data.remote.responses.disease

import com.google.gson.annotations.SerializedName

data class DiseaseResponses(

	@field:SerializedName("diseaseName")
	val diseaseName: String? = null,

	@field:SerializedName("plantNames")
	val plantNames: String? = null,

	@field:SerializedName("causes")
	val causes: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("prevention")
	val prevention: String? = null
)
