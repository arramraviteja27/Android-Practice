package com.design.practice.data.mapper

import com.design.practice.data.remote.dto.LoginResponse
import com.design.practice.domain.model.User


fun LoginResponse.toDomain(): User {

    return User(

        id = id,

        username = username,

        email = email,

        firstName = firstName,

        lastName = lastName,

        image = image,

        accessToken = accessToken,

        refreshToken = refreshToken

    )

}