package com.design.practice.domain.repository

import com.design.practice.core.common.ApiState
import com.design.practice.data.remote.dto.LoginRequest
import com.design.practice.domain.model.User


interface AuthRepository {

    suspend fun login(

        request: LoginRequest

    ): ApiState<User>

}