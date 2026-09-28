package com.design.practice.domain.usecase

import com.design.practice.domain.model.ChatMessage
import com.design.practice.domain.repository.ChatRepository
import jakarta.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    suspend operator fun invoke(
        message: String,
        previousMessages: List<ChatMessage>
    ): Result<String> {
        return repository.sendMessage(
            message = message,
            previousMessages = previousMessages
        )
    }
}