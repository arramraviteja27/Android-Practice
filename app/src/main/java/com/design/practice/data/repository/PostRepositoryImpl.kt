package com.design.practice.data.repository

import com.design.practice.data.model.toPost
import com.design.practice.data.remote.api.AuthApi
import com.design.practice.domain.model.Post
import com.design.practice.domain.repository.PostRepository
import jakarta.inject.Inject

class PostRepositoryImpl @Inject constructor(

    private val authApi : AuthApi
) : PostRepository {
    override suspend fun getPosts(): List<Post> {

        return authApi.getPosts().map {

            it.toPost()
        }
    }


}