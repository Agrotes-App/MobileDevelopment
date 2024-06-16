package com.example.agrotes_mobile.data.remote.responses.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponses(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("createdUser")
	val createdUser: CreatedUser? = null
)

data class CreatedUser(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("profile_Photo")
	val profilePhoto: Any? = null,

	@field:SerializedName("updateAt")
	val updateAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
