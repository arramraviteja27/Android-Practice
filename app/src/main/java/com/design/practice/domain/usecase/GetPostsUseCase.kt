package com.design.practice.domain.usecase

import com.design.practice.domain.model.Post
import com.design.practice.domain.repository.PostRepository
import jakarta.inject.Inject

class GetPostsUseCase @Inject constructor(
      private val repository: PostRepository
) {

    suspend operator fun invoke() : List<Post> {

          return repository.getPosts()
    }
}