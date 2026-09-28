package com.design.practice.data.repository

import com.design.practice.data.model.Todo
import com.design.practice.data.model.toPost
import com.design.practice.data.remote.api.AuthApi
import com.design.practice.domain.model.Post
import com.design.practice.domain.repository.PostRepository
import com.design.practice.domain.repository.TodoRepository
import jakarta.inject.Inject

class TodoRepositoryImpl @Inject constructor(

    private val authApi : AuthApi
) : TodoRepository {

    override suspend fun getTodos(): List<Todo> {
        return authApi.getTodos()
    }


}