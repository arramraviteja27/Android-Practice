package com.design.practice.presentation.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.design.practice.core.common.ApiState
import com.design.practice.domain.model.Post
import com.design.practice.domain.usecase.GetPostsUseCase
import com.design.practice.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PostViewModel @Inject constructor(

    private val getPostUseCase: GetPostsUseCase
) : ViewModel(){

    var postState by mutableStateOf<ApiState<List<Post>>>(ApiState.Idle)
      private set

    init {

        getPosts()
    }

    fun getPosts(){

        viewModelScope.launch {

            postState = ApiState.Loading

            try {

                val posts = getPostUseCase.invoke()

                postState = ApiState.Success(posts)

            }catch (e : Exception) {

                postState = ApiState.Failure(
                    e.message ?: "Unknown Error"
                )

            }
        }
    }
}