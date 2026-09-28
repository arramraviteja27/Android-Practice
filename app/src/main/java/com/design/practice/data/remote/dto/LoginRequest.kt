package com.design.practice.data.remote.dto

data class LoginRequest(

    val username: String,

    val password: String,

    val expiresInMins: Int = 60
)