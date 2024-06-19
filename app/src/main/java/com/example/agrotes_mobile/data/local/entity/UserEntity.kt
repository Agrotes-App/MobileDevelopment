package com.example.agrotes_mobile.data.local.entity

data class UserEntity (
    val token: String,
    val name: String,
    val userId: String,
    val isLogin: Boolean = false
)