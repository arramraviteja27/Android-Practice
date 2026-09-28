package com.design.practice.data.remote.api

import com.design.practice.data.model.PostDto
import com.design.practice.data.model.Todo
import com.design.practice.data.remote.dto.LoginRequest
import com.design.practice.data.remote.dto.LoginResponse
import com.design.practice.domain.model.Post
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(

        @Body request : LoginRequest
    ) : LoginResponse

    @GET("posts")
    suspend fun getPosts() : List<PostDto>


    @GET("todos")
    suspend fun getTodos() : List<Todo>
}