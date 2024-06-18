package com.example.agrotes_mobile.data.remote.responses.auth

import com.google.gson.annotations.SerializedName

data class UpdateResponses(

	@field:SerializedName("userUpdate")
	val userUpdate: UserUpdate? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserUpdate(

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
