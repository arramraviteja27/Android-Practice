package com.design.practice.domain.repository

import com.design.practice.domain.model.Post

interface PostRepository {

    suspend fun getPosts() : List<Post>
}