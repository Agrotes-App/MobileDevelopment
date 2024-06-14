package com.example.agrotes_mobile.data.pref

data class UserModel (
    val token: String,
    val name: String,
    val userId: String,
    val isLogin: Boolean = false
)