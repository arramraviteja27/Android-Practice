package com.design.practice.domain.model


data class User(

    val id: Int,

    val username: String,

    val email: String,

    val firstName: String,

    val lastName: String,

    val image: String,

    val accessToken: String,

    val refreshToken: String

)