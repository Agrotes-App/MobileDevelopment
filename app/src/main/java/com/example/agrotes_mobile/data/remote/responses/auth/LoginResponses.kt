package com.example.agrotes_mobile.data.remote.responses.auth

import com.google.gson.annotations.SerializedName
import java.lang.Error

data class LoginResponses(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class LoginRequest(
	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	val password: String? = null,
)

data class User(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
