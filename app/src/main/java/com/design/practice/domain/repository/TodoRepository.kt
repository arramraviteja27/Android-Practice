package com.design.practice.domain.repository

import com.design.practice.data.model.Todo
import com.design.practice.domain.model.Post

interface TodoRepository {

    suspend fun getTodos() : List<Todo>
}