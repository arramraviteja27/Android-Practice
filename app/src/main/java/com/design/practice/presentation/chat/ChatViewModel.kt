package com.design.practice.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.design.practice.core.common.Constants
import com.design.practice.domain.model.ChatMessage
import com.design.practice.domain.usecase.SendMessageUseCase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.PublicPreviewAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())

    val uiState = _uiState.asStateFlow()


    fun onInputChanged(text: String) {

        _uiState.update {
            it.copy(inputText = text)
        }
    }

    fun sendMessage() {

        val message = _uiState.value.inputText.trim()

        if (message.isEmpty()) return

        val previousMessages = _uiState.value.messages

        val userMessage = ChatMessage(
            text = message,
            isFromUser = true
        )

        _uiState.update {

            it.copy(

                messages = it.messages + userMessage,
                inputText = "",
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {

            sendMessageUseCase(
                message = message,
                previousMessages = previousMessages
            )
                .onSuccess { response ->

                    val botMessage = ChatMessage(
                        text = response,
                        isFromUser = false
                    )

                    _uiState.update {

                        it.copy(
                            messages = it.messages + botMessage,
                            isLoading = false
                        )
                    }


                }

                .onFailure { exception ->

                    Log.i("chatviewmodel",exception.toString())

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Something went wrong"
                        )
                    }
                }
        }
    }


}