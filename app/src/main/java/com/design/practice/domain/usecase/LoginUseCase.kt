package com.design.practice.domain.usecase

import com.design.practice.core.common.ApiState
import com.design.practice.data.remote.dto.LoginRequest
import com.design.practice.domain.model.User
import com.design.practice.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(

    private val repository: AuthRepository

) {

    suspend operator fun invoke(

        request: LoginRequest

    ): ApiState<User> {

        return repository.login(request)

    }

}