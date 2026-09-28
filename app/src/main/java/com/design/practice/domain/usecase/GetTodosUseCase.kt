package com.design.practice.domain.usecase

import com.design.practice.data.model.Todo
import com.design.practice.domain.model.Post
import com.design.practice.domain.repository.PostRepository
import com.design.practice.domain.repository.TodoRepository
import jakarta.inject.Inject

class GetTodosUseCase @Inject constructor(
      private val repository: TodoRepository
) {

    suspend operator fun invoke() : List<Todo> {

          return repository.getTodos()
    }
}